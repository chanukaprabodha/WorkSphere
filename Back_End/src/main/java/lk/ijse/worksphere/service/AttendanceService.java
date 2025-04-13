package lk.ijse.worksphere.service;

import lk.ijse.worksphere.dto.AttendanceDTO;

import java.util.List;

public interface AttendanceService {
    void clockIn(String employeeIdFromToken);
    void clockOut(String employeeIdFromToken);
    List<AttendanceDTO> getLastTwoAttendanceRecords(String employeeId);

    List<AttendanceDTO> getAttendanceByDateRange(String token, String fromDate, String toDate);

    List<AttendanceDTO> getMonthlyAttendance(String token);
}
