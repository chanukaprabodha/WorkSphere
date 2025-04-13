package lk.ijse.worksphere.service.impl;

import lk.ijse.worksphere.dto.DepartmentDTO;
import lk.ijse.worksphere.entity.Department;
import lk.ijse.worksphere.repository.DepartmentRepo;
import lk.ijse.worksphere.service.DepartmentService;
import lk.ijse.worksphere.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-23
 * Time: 11:32 AM
 */

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveDepartment(DepartmentDTO departmentDTO) {
        String generatedId ;
        do {
            generatedId = IdGenerator.generateId("DEP");
        } while (departmentRepo.existsById(generatedId));
        departmentDTO.setId(generatedId);
        try {
            departmentRepo.save(modelMapper.map(departmentDTO, Department.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e);
        }
    }

    @Override
    public void updateDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepo.existsById(departmentDTO.getId())) {
            departmentRepo.save(modelMapper.map(departmentDTO, Department.class));
        }
        throw new RuntimeException("Department not found");
    }
}
