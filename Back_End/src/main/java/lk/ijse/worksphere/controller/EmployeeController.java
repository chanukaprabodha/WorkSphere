package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.EmployeeDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.dto.UserDTO;
import lk.ijse.worksphere.service.EmployeeService;
import lk.ijse.worksphere.util.JwtUtil;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-16
 * Time: 09:04 AM
 */

@RestController
@RequestMapping("api/v1/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE
})
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "save")
    public ResponseEntity<ResponseDTO> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.saveEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        VarList.Created,
                        "Employee saved successfully",
                        employeeDTO));
    }

    @PutMapping(path = "update")
    public ResponseEntity<ResponseDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        VarList.Created,
                        "Employee updated successfully",
                        employeeDTO));
    }

    @DeleteMapping(path = "delete/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ResponseDTO> deleteEmployee(@PathVariable String id) {

        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        VarList.Created,
                        "Employee deleted successfully",
                        null));
    }

    @GetMapping(path = "find/{id}")
    public ResponseEntity<ResponseDTO> findEmployee(@PathVariable String id) {
        EmployeeDTO employeeDTO = employeeService.findEmployee(id);
        if (employeeDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(
                            VarList.Not_Found,
                            "Employee not found",
                            null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(
                        VarList.OK,
                        "Employee found successfully",
                        employeeDTO));
    }

    @GetMapping(path = "getAll")
    public ResponseEntity<ResponseDTO> getAllEmployee() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        VarList.OK,
                        "Employee fetched successfully",
                        employeeService.getAllEmployee()));
    }

    @GetMapping(path = "getEmployeeDetails")
    public ResponseEntity<ResponseDTO> getLoggedInEmployee(@RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.getUsernameFromToken(token.substring(7));
        EmployeeDTO employee = employeeService.getDetailsFromLoggedInUser(usernameFromToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(
                        VarList.OK,
                        "Employee fetched successfully",
                        employee));
    }

    @GetMapping(path = "upcomingBirthdays")
    public ResponseEntity<ResponseDTO> upcomingBirthday(@RequestHeader("Authorization") String token) {
        System.out.println("Finding upcoming birthdays in controller");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(
                        VarList.OK,
                        "Upcoming birthdays fetched successfully",
                        employeeService.upcomingBirthday(token)));
    }

    @GetMapping(path = "allBirthdays")
    public ResponseEntity<ResponseDTO> allBirthdays(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(
                        VarList.OK,
                        "All birthdays fetched successfully",
                        employeeService.allBirthdays(token)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "getTotalEmployeeCount")
    public ResponseEntity<ResponseDTO> getEmployeeCount(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(
                        VarList.OK,
                        "Employee count fetched successfully",
                        employeeService.getEmployeeCount(token)));
    }

}
