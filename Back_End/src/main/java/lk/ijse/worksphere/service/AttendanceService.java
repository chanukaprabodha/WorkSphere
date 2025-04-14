package lk.ijse.worksphere.service;

import lk.ijse.worksphere.dto.AttendanceDTO;
import lk.ijse.worksphere.entity.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    void clockIn(String employeeIdFromToken);
    void clockOut(String employeeIdFromToken);
    List<AttendanceDTO> getLastTwoAttendanceRecords(String employeeId);

    List<AttendanceDTO> getAttendanceByDateRange(String token, String fromDate, String toDate);

    List<AttendanceDTO> getMonthlyAttendance(String token);

    String getAttendanceStatus(String token);
}
