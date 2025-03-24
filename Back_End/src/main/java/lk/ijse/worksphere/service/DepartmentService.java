package lk.ijse.worksphere.service;

import lk.ijse.worksphere.dto.DepartmentDTO;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 11:32 AM
 */

public interface DepartmentService {
    void saveDepartment(DepartmentDTO departmentDTO);
    void updateDepartment(DepartmentDTO departmentDTO);
}
