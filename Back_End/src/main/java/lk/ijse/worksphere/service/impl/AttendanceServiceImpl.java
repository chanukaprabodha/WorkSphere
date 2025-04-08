package lk.ijse.worksphere.service.impl;

import io.swagger.v3.oas.annotations.servers.Server;
import lk.ijse.worksphere.dto.AttendanceDTO;
import lk.ijse.worksphere.entity.Attendance;
import lk.ijse.worksphere.repository.AttendanceRepo;
import lk.ijse.worksphere.service.AttendanceService;
import lk.ijse.worksphere.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AttendanceRepo attendanceRepo;

    @Autowired
    private ModelMapper modelMapper;


    public void clockIn(String employeeIdFromToken) {
        String generatedId;
        do {
            generatedId = IdGenerator.generateId("AT-");
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

    public List<AttendanceDTO> getLastTwoAttendanceRecords(String employeeId) {
        List<Attendance> lastTwoRecords = attendanceRepo.findTop2ByEmployeeIdOrderByDateDescInTimeDesc(employeeId);
        return lastTwoRecords.stream()
                .map(attendance -> modelMapper.map(attendance, AttendanceDTO.class))
                .toList();
    }
}
