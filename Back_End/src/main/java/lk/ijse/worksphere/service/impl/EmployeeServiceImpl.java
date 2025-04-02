package lk.ijse.worksphere.service.impl;

import lk.ijse.worksphere.dto.EmployeeDTO;
import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.repository.EmployeeRepo;
import lk.ijse.worksphere.service.EmployeeService;
import lk.ijse.worksphere.util.IdGenerator;
import lk.ijse.worksphere.util.VarList;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-17
 * Time: 10:09 PM
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {
        String generatedId ;
        do {
            generatedId = IdGenerator.generateId("EMP-");
        } while (employeeRepo.existsById(generatedId));
        employeeDTO.setId(generatedId);
        try {
            employeeRepo.save(modelMapper.map(employeeDTO, Employee.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public EmployeeDTO findEmployee(String id) {
        Employee employee = employeeRepo.findById(id).get();
        return modelMapper.map(employee, EmployeeDTO.class);
    }*/
    @Override
    public EmployeeDTO findEmployee(String id) {
        System.out.println("Finding employee with id: " + id);
        Optional<Employee> optionalEmployee = employeeRepo.findById(id);
        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("Employee not found for id: " + id);
        }
        Employee employee = optionalEmployee.get();
        System.out.println("Employee found: " + employee);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepo.existsById(employeeDTO.getId())) {
            employeeRepo.save(modelMapper.map(employeeDTO, Employee.class));
            // log
        }
        throw new RuntimeException("Employee not found");
    }

    @Override
    public void deleteEmployee(String id) {
        if (employeeRepo.existsById(id)) {
            employeeRepo.deleteById(id);
        }
        throw new RuntimeException("Employee not found");
    }

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        return modelMapper.map(
                employeeRepo.findAll(),
                new TypeToken<List<EmployeeDTO>>(){}.getType()
        );
    }

    /*@Override
    public EmployeeDTO getDetailsFromLoggedInUser(String usernameFromToken) {
        System.out.println("usernameFromToken = " + usernameFromToken);

        Employee employee = employeeRepo.findByEmail(usernameFromToken);
        if (employee == null) {
            throw new RuntimeException("Employee not found for email: " + usernameFromToken);
        }

        System.out.println(employee);

        return modelMapper.map(employee, EmployeeDTO.class);
    }*/

    @Override
    public EmployeeDTO getDetailsFromLoggedInUser(String usernameFromToken) {
        System.out.println("usernameFromToken = " + usernameFromToken);
        Optional<Employee> optionalEmployee = employeeRepo.findByEmail(usernameFromToken);
        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("Employee not found for email: " + usernameFromToken);
        }
        Employee employee = optionalEmployee.get();
        System.out.println("Employee found: " + employee);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

}
