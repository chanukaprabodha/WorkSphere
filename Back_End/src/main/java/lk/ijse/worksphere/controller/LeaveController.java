package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.LeaveRequestDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.service.LeaveService;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public ResponseEntity<ResponseDTO> saveLeave(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        leaveService.saveLeave(leaveRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(
                VarList.Created,
                "Leave saved successfully",
                leaveRequestDTO));
    }
}
