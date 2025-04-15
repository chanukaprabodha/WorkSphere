package lk.ijse.worksphere.service.impl;

import lk.ijse.worksphere.dto.EmployeeDTO;
import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.repository.EmployeeRepo;
import lk.ijse.worksphere.service.EmployeeService;
import lk.ijse.worksphere.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        String generatedId;
        do {
            generatedId = IdGenerator.generateId("EMP");
        } while (employeeRepo.existsById(generatedId));
        employeeDTO.setId(generatedId);
        // Set default leave values only if not already set
        if (employeeDTO.getAnnualLeaves() == 0) employeeDTO.setAnnualLeaves(14);
        if (employeeDTO.getCasualLeave() == 0) employeeDTO.setCasualLeave(7);
        if (employeeDTO.getSickLeave() == 0) employeeDTO.setSickLeave(7);

        // Set leaveBalance as the sum of the above
        employeeDTO.setLeaveBalance(
                employeeDTO.getAnnualLeaves() +
                        employeeDTO.getCasualLeave() +
                        employeeDTO.getSickLeave()
        );
        try {
            Employee employee = modelMapper.map(employeeDTO, Employee.class);
            employeeRepo.save(employee);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving employee: " + e.getMessage(), e);
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
                new TypeToken<List<EmployeeDTO>>() {
                }.getType()
        );
    }

    @Override
    public EmployeeDTO getDetailsFromLoggedInUser(String usernameFromToken) {
        Optional<Employee> optionalEmployee = employeeRepo.findByEmail(usernameFromToken);
        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("Employee not found for email: " + usernameFromToken);
        }
        Employee employee = optionalEmployee.get();
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    public List<EmployeeDTO> upcomingBirthday(String usernameFromToken) {
        System.out.println("Finding upcoming birthdays in service");
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        return employeeRepo.findUpcomingBirthdays(month, day)
                .stream()
                .map(emp -> {
                    EmployeeDTO dto = modelMapper.map(emp, EmployeeDTO.class);
                    if (emp.getDepartment() != null) {
                        dto.setDepartmentId(emp.getDepartment().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> allBirthdays(String usernameFromToken) {
        return employeeRepo.findAllByBirthdayOrder()
                .stream()
                .map(emp -> {
                    EmployeeDTO dto = modelMapper.map(emp, EmployeeDTO.class);
                    if (emp.getDepartment() != null) {
                        dto.setDepartmentId(emp.getDepartment().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int getEmployeeCount(String token) {
        int size = employeeRepo.findAll().size();
        System.out.println("Employee count: " + size);
        return size;
    }

}
