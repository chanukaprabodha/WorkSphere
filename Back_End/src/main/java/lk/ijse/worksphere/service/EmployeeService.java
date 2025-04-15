package lk.ijse.worksphere.service;

import lk.ijse.worksphere.dto.EmployeeDTO;

import java.util.List;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-17
 * Time: 10:12 PM
*/

public interface EmployeeService {
    void saveEmployee(EmployeeDTO employeeDTO);

    void updateEmployee(EmployeeDTO employeeDTO);

    void deleteEmployee(String id);

    EmployeeDTO findEmployee(String id);

    List<EmployeeDTO> getAllEmployee();

    EmployeeDTO getDetailsFromLoggedInUser(String usernameFromToken);

    List<EmployeeDTO> upcomingBirthday(String usernameFromToken);

    List<EmployeeDTO> allBirthdays(String usernameFromToken);

    int getEmployeeCount(String token);
}
