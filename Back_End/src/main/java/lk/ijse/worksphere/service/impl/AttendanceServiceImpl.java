package lk.ijse.worksphere.service.impl;

import lk.ijse.worksphere.dto.AttendanceDTO;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
    public void clockIn(String employeeIdFromToken) {
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
    public void clockOut(String employeeIdFromToken) {
        Optional<Attendance> optionalAttendance = attendanceRepo.findTopByEmployeeIdOrderByDateDescInTimeDesc(employeeIdFromToken);

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
    public List<AttendanceDTO> getLastTwoAttendanceRecords(String employeeId) {
        List<Attendance> lastTwoRecords = attendanceRepo.findTop2ByEmployeeIdOrderByDateDescInTimeDesc(employeeId);
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

    @Scheduled(cron = "0 0 18 * * ?") // Every day at 6 PM
    public void evaluateDailyAttendance() {
        LocalDate today = LocalDate.now();
        List<Employee> allEmployees = employeeRepo.findAll();

        for (Employee employee : allEmployees) {
            boolean recordExists = attendanceRepo.existsByEmployeeIdAndDate(employee.getId(), today);
            if (!recordExists) {
                Attendance.Status status = Attendance.Status.ABSENT;

                Optional<Leave> leaveOpt = leaveRepo.findByEmployeeIdAndDate(employee.getId(), today);
                if (leaveOpt.isPresent() && leaveOpt.get().getStatus() == Leave.Status.APPROVED || leaveOpt.get().getStatus() == Leave.Status.PENDING) {
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
