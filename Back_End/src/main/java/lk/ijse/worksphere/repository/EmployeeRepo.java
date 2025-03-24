package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-17
 * Time: 10:13 PM
 */

public interface EmployeeRepo extends JpaRepository<Employee, String> {
}
