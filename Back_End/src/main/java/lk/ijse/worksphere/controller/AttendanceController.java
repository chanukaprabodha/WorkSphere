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
    private AttendanceServiceImpl attendanceService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("clockIn")
    public ResponseEntity<ResponseDTO>clockIn(@RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.getUsernameFromToken(token.substring(7));
        EmployeeDTO detailsFromLoggedInUser = employeeService.getDetailsFromLoggedInUser(usernameFromToken);
        String employeeIdFromToken = detailsFromLoggedInUser.getId();

        if (employeeIdFromToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(
                    VarList.Unauthorized,
                    "Invalid or expired token",
                    null
            ));
        }

        System.out.println("Employee ID from token: " + employeeIdFromToken);
        attendanceService.clockIn(employeeIdFromToken);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Clocked in successfully",
                null));
    }

    @PostMapping("clockOut")
    public ResponseEntity<ResponseDTO> clockOut(@RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.getUsernameFromToken(token.substring(7));
        EmployeeDTO detailsFromLoggedInUser = employeeService.getDetailsFromLoggedInUser(usernameFromToken);
        String employeeIdFromToken = detailsFromLoggedInUser.getId();
        attendanceService.clockOut(employeeIdFromToken);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Clocked out successfully",
                null));
    }

    @GetMapping("lastTwoRecords")
    public ResponseEntity<ResponseDTO> getLastTwoAttendanceRecords(@RequestHeader("Authorization") String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token.substring(7));
        List<AttendanceDTO> attendanceRecords = attendanceService.getLastTwoAttendanceRecords(employeeIdFromToken);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Last two attendance records fetched successfully",
                attendanceRecords));
    }
}
