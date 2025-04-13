package lk.ijse.worksphere.util;

import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.repository.EmployeeRepo;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveBalanceReset {

    @Autowired
    EmployeeRepo employeeRepo;

    @Scheduled(cron = "0 0 0 1 1 *")
    public void resetLeaveBalance() {
        List<Employee> employees = employeeRepo.findAll();
        for (Employee employee : employees) {
            employee.setAnnualLeaves(14);
            employee.setCasualLeave(7);
            employee.setSickLeave(7);
        }
        employeeRepo.saveAll(employees);
    }

}
