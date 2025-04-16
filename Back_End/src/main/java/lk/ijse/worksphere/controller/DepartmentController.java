package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.DepartmentDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.service.DepartmentService;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 11:14 AM
 */

@RestController
@RequestMapping("api/v1/department")
@CrossOrigin("*")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "save")
    public ResponseEntity<ResponseDTO> saveDepartment(@RequestBody DepartmentDTO departmentDTO) {
        departmentService.saveDepartment(departmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        VarList.Created,
                        "Department saved successfully",
                        departmentDTO));
    }

    @PutMapping(path = "update")
    public ResponseEntity<ResponseDTO> updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        departmentService.updateDepartment(departmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        VarList.Created,
                        "Department updated successfully",
                        departmentDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllDepartments() {
        try {
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", departments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Error: " + e.getMessage(), null));
        }
    }
}
