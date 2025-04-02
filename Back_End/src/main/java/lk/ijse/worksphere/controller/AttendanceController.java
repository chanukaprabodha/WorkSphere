package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.AttendanceDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.service.AttendanceService;
import lk.ijse.worksphere.service.impl.AttendanceServiceImpl;
import lk.ijse.worksphere.util.JwtUtil;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AttendanceController {
    @Autowired
    private AttendanceServiceImpl attendanceService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("clockIn")
    public ResponseEntity<ResponseDTO>clockIn(@RequestBody String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token);
        attendanceService.clockIn(employeeIdFromToken);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Clocked in successfully",
                null));
    }

    @PostMapping("clockOut")
    public ResponseEntity<ResponseDTO> clockOut(@RequestBody String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token);
        attendanceService.clockOut(employeeIdFromToken);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Clocked out successfully",
                null));
    }

    @GetMapping("pastTwoDays")
    public ResponseEntity<ResponseDTO> getPastTwoDaysAttendance(@RequestHeader("Authorization") String token) {
        String employeeIdFromToken = jwtUtil.getEmployeeIdFromToken(token);
        List<AttendanceDTO> attendanceRecords = attendanceService.getPastTwoDaysAttendance(employeeIdFromToken);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Past two days' attendance fetched successfully",
                attendanceRecords));
    }
}
