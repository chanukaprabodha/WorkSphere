package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.LeaveDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.entity.Leave;
import lk.ijse.worksphere.service.LeaveService;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path = "recentLeaves")
    public ResponseEntity<ResponseDTO> getRecentLeave(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Leave history retrieved successfully",
                leaveService.getRecentLeave(token)));
    }

    @GetMapping(path = "history")
    public ResponseEntity<ResponseDTO> getLeaveHistory(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Leave history retrieved successfully",
                leaveService.getLeaveHistory(token)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "getPendingLeaveCount")
    public ResponseEntity<ResponseDTO> getPendingLeaveCount(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Pending leave count retrieved successfully",
                leaveService.getPendingLeaveCount()));
    }

    @GetMapping(path = "pending")
    public ResponseEntity<ResponseDTO> getPendingLeave(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Pending leave retrieved successfully",
                leaveService.getPendingLeave()));
    }

    @PutMapping(path = "updateLeave/{leaveId}")
    public ResponseEntity<ResponseDTO> updateLeave(@RequestHeader("Authorization") String token,
                                                   @PathVariable String leaveId,
                                                   @RequestParam Leave.Status status) {
        leaveService.updateLeaveStatus(leaveId, status);
        return ResponseEntity.ok(new ResponseDTO(
                VarList.OK,
                "Leave updated successfully",
                null));
    }
}
