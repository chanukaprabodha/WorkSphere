package lk.ijse.worksphere.controller;


import lk.ijse.worksphere.dto.EmployeeDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.service.EmployeeService;
import lk.ijse.worksphere.util.FileUploadUtil;
import lk.ijse.worksphere.util.JwtUtil;
import lk.ijse.worksphere.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


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

    @PostMapping(path = "save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveEmployee(
            @RequestParam("profilePicture") MultipartFile file,
            @ModelAttribute EmployeeDTO employeeDTO,
            @RequestHeader("Authorization") String token) throws IOException {

        String uploadDir = "profile_pictures/";
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String actualFilePath = FileUploadUtil.getFileName(uploadDir, fileName, file);

        System.out.println("File path: " + actualFilePath);

        employeeService.saveEmployee(employeeDTO, actualFilePath);

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


    @GetMapping("search")
    public ResponseEntity<ResponseDTO> searchEmployees(@RequestParam String keyword) {
        List<EmployeeDTO> employees = employeeService.searchEmployees(keyword);
        return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Search successful", employees));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("no-access")
    public ResponseEntity<ResponseDTO> getEmployeesWithoutSystemAccess() {
        return ResponseEntity.ok(
                new ResponseDTO(VarList.OK,
                        "Search successful",
                        employeeService.getEmployeesWithoutUserAccounts()));
    }

}
