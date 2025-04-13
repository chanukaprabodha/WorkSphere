package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.LeaveDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.service.LeaveService;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 02:30 PM
 */

@RestController
@RequestMapping("api/v1/leave")
@CrossOrigin("*")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;


    @PostMapping("apply")
    public ResponseEntity<ResponseDTO> applyLeave(@RequestHeader("Authorization") String token,@RequestBody LeaveDTO leaveDTO) {
        System.out.println(leaveDTO);
        leaveService.applyLeave(token,leaveDTO);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Leave applied successfully",
                null));
    }

    @PutMapping("approve/{id}")
    public ResponseEntity<ResponseDTO> approveLeave(@PathVariable String id) {
        leaveService.approveLeave(id);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Leave approved successfully",
                null));
    }

    @PutMapping("reject/{id}")
    public ResponseEntity<ResponseDTO> rejectLeave(@RequestHeader("Authorization") String token,@PathVariable String id) {
        leaveService.rejectLeave(token,id);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Leave rejected successfully",
                null));
    }

    @GetMapping("types")
    public ResponseEntity<ResponseDTO> getLeaveTypes() {
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Leave types retrieved successfully",
                leaveService.getLeaveTypes()));
    }
}
