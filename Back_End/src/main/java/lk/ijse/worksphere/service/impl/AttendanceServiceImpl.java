package lk.ijse.worksphere.service.impl;

import lk.ijse.worksphere.dto.AttendanceDTO;
import lk.ijse.worksphere.dto.PublicHolidayDTO;
import lk.ijse.worksphere.entity.Attendance;
import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.entity.Leave;
import lk.ijse.worksphere.repository.AttendanceRepo;
import lk.ijse.worksphere.repository.EmployeeRepo;
import lk.ijse.worksphere.repository.LeaveRepo;
import lk.ijse.worksphere.service.AttendanceService;
import lk.ijse.worksphere.util.IdGenerator;
import lk.ijse.worksphere.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private LeaveRepo leaveRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public void clockIn(String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));

        // Check if already clocked in and out today
        Optional<Attendance> completedToday = attendanceRepo.findCompletedAttendanceToday(employeeIdFromToken);
        if (completedToday.isPresent()) {
            throw new RuntimeException("You have already clocked in and out for today.");
        }

        // Check if already clocked in but not clocked out yet
        Optional<Attendance> ongoing = attendanceRepo.findTopByEmployeeIdAndDateOrderByInTimeDesc(employeeIdFromToken, LocalDate.now());
        if (ongoing.isPresent() && ongoing.get().getOutTime() == null) {
            throw new RuntimeException("Already clocked in. Please clock out before trying again.");
        }

        String generatedId;
        do {
            generatedId = IdGenerator.generateId("ATT");
        } while (attendanceRepo.existsById(generatedId));
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setId(generatedId);
        attendanceDTO.setDate(LocalDate.now());
        attendanceDTO.setInTime(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
        attendanceDTO.setEmployeeId(employeeIdFromToken);
        attendanceDTO.setStatus("PRESENT");
        try {
            attendanceRepo.save(modelMapper.map(attendanceDTO, Attendance.class));
        } catch (Exception e) {
            throw new RuntimeException("Error while saving attendance: " + e.getMessage(), e);
        }
    }

    @Override
    public void clockOut(String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        Optional<Attendance> optionalAttendance = attendanceRepo.findTopByEmployeeIdOrderByDateDescInTimeDesc(employeeIdFromToken);

        if (optionalAttendance.isEmpty() || optionalAttendance.get().getOutTime() != null) {
            throw new RuntimeException("You must clock in before clocking out.");
        }

        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();
            attendance.setOutTime(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));

            if (attendance.getInTime() != null) {
                Duration duration = Duration.between(attendance.getInTime(), attendance.getOutTime());

                // Convert duration to hours and minutes format (HH:mm)
                LocalTime totalHours = LocalTime.ofNanoOfDay(duration.toNanos());
                attendance.setTotalHours(Time.valueOf(totalHours));
            }

            try {
                attendanceRepo.save(attendance);
            } catch (Exception e) {
                throw new RuntimeException("Error while updating attendance: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("No clock-in record found for employee: " + employeeIdFromToken);
        }
    }

    @Override
    public List<AttendanceDTO> getLastTwoAttendanceRecords(String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        List<Attendance> lastTwoRecords = attendanceRepo.findTop2ByEmployeeIdOrderByDateDescInTimeDesc(employeeIdFromToken);
        return lastTwoRecords.stream()
                .map(attendance -> modelMapper.map(attendance, AttendanceDTO.class))
                .toList();
    }

    @Override
    public List<AttendanceDTO> getAttendanceByDateRange(String token, String fromDate, String toDate) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        System.out.println("Employee ID from token: " + employeeIdFromToken);
        List<Attendance> records = attendanceRepo.findByEmployeeIdAndDateBetween(employeeIdFromToken, LocalDate.parse(fromDate), LocalDate.parse(toDate));
        if (records.isEmpty()) {
            throw new RuntimeException("No attendance records found for the given date range.");
        }
        System.out.println("Attendance records: " + records);
        return records.stream()
                .map(record -> modelMapper.map(record, AttendanceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getMonthlyAttendance(String token) {

        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<Attendance> records = attendanceRepo.findByEmployeeIdAndDateBetween(employeeIdFromToken, firstDayOfMonth, lastDayOfMonth);
        if (records.isEmpty()) {
            throw new RuntimeException("No attendance records found for the current month.");
        }
        return records.stream()
                .map(record -> modelMapper.map(record, AttendanceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String getAttendanceStatus(String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        List<Attendance> todayClockIns = attendanceRepo.findAllClockInTodayWithoutOut(employeeIdFromToken);

        System.out.println("Today clock-ins: " + todayClockIns);

        if (todayClockIns.isEmpty()) {
            return "clockIn"; // No open clock-in today → Show Clock In
        }

        Attendance latest = todayClockIns.get(0); // Already ordered by inTime DESC
        if (latest.getInTime() != null && latest.getOutTime() == null) {
            return "clockOut"; // Show Clock Out button
        }

        return "clockIn"; // Fallback

    }

    @Override
    public int getTodayClockInCount() {
        int countTodayClockIns = attendanceRepo.countTodayClockIns();
        System.out.println(countTodayClockIns);
        return countTodayClockIns;
    }

    public boolean isTodayPublicHoliday() {
        RestTemplate restTemplate = new RestTemplate();
        int year = Year.now().getValue();
        String countryCode = "LK";
        String url = "https://date.nager.at/api/v3/PublicHolidays/" + year + "/" + countryCode;

        try {
            ResponseEntity<PublicHolidayDTO[]> response = restTemplate.getForEntity(url, PublicHolidayDTO[].class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                LocalDate today = LocalDate.now();
                for (PublicHolidayDTO holiday : response.getBody()) {
                    if (holiday.getDate().isEqual(today)) {
                        return true; // 🎉 Today is a public holiday
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle failure gracefully
        }

        return false; // Not a public holiday
    }

    @Scheduled(cron = "0 0 18 * * ?", zone = "Asia/Colombo") // Every day at 6 PM
    public void evaluateDailyAttendance() {
        LocalDate today = LocalDate.now();

        // Skip if it's Sunday
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY || isTodayPublicHoliday()) {
            return;
        }

        List<Employee> allEmployees = employeeRepo.findAll();

        for (Employee employee : allEmployees) {
            boolean recordExists = attendanceRepo.existsByEmployeeIdAndDate(employee.getId(), today);
            if (!recordExists) {
                Attendance.Status status = Attendance.Status.ABSENT;

                Optional<Leave> leaveOpt = leaveRepo.findByEmployeeIdAndDate(employee.getId(), today);
                if (leaveOpt.isPresent() &&
                        (leaveOpt.get().getStatus() == Leave.Status.APPROVED || leaveOpt.get().getStatus() == Leave.Status.PENDING)) {
                    status = Attendance.Status.LEAVE;
                }

                Attendance attendance = new Attendance();
                attendance.setId(IdGenerator.generateId("ATT"));
                attendance.setEmployee(employee);
                attendance.setDate(today);
                attendance.setStatus(status);
                attendanceRepo.save(attendance);
            }
        }
    }

}
