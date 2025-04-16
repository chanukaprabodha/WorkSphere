package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.AttendanceDTO;
import lk.ijse.worksphere.dto.EmployeeDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.service.AttendanceService;
import lk.ijse.worksphere.service.EmployeeService;
import lk.ijse.worksphere.service.impl.AttendanceServiceImpl;
import lk.ijse.worksphere.util.JwtUtil;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/attendance")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AttendanceController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    JwtUtil jwtUtil;

    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    @PostMapping("clockIn")
    public ResponseEntity<ResponseDTO>clockIn(@RequestHeader("Authorization") String token) {

        try {
            attendanceService.clockIn(token);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK,"Clocked in successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(VarList.Conflict, e.getMessage(), false));
        }
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    @PostMapping("clockOut")
    public ResponseEntity<ResponseDTO> clockOut(@RequestHeader("Authorization") String token) {
        attendanceService.clockOut(token);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Clocked out successfully",
                null));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    @GetMapping("lastTwoRecords")
    public ResponseEntity<ResponseDTO> getLastTwoAttendanceRecords(@RequestHeader("Authorization") String token) {
        List<AttendanceDTO> attendanceRecords = attendanceService.getLastTwoAttendanceRecords(token);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Last two attendance records fetched successfully",
                attendanceRecords));
    }

    @GetMapping("getAttendanceByRange")
    public ResponseEntity<ResponseDTO> getAttendanceByDateRange(
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestHeader("Authorization") String token) {

        System.out.println("From Date: " + fromDate + ", To Date: " + toDate + ", Token: " + token);
        List<AttendanceDTO> records = attendanceService.getAttendanceByDateRange(token, fromDate, toDate);

        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Success",
                records));
    }

    @GetMapping(path = "getMonthlyAttendance")
    public ResponseEntity<ResponseDTO> getMonthlyAttendance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Success",
                attendanceService.getMonthlyAttendance(token)));
    }

    @GetMapping(path = "status")
    public ResponseEntity<ResponseDTO> getAttendanceStatus(@RequestHeader("Authorization") String token) {
        String attendanceStatus = attendanceService.getAttendanceStatus(token);
        System.out.println("Attendance status :- " + attendanceStatus);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Success",
                attendanceStatus));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "getActiveEmployeeCount")
    public ResponseEntity<ResponseDTO> getActiveEmployeeCount(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(
                        VarList.OK,
                        "Active employee count fetched successfully",
                        attendanceService.getTodayClockInCount()));
    }

}
