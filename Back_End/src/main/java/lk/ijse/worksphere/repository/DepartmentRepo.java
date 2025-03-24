package lk.ijse.worksphere.repository;

import lk.ijse.worksphere.dto.DepartmentDTO;
import lk.ijse.worksphere.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 02:12 PM
 */

public interface DepartmentRepo extends JpaRepository<Department, String> {
}
