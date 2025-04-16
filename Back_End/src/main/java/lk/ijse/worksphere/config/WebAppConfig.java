package lk.ijse.worksphere.config;

import lk.ijse.worksphere.dto.EmployeeDTO;
import lk.ijse.worksphere.dto.LeaveDTO;
import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.entity.Leave;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 02:30 PM
 */

@Configuration
public class WebAppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping from Employee to EmployeeDTO
        modelMapper.typeMap(Employee.class, EmployeeDTO.class)
                    .addMapping(src -> src.getDepartment().getName(), EmployeeDTO::setDepartmentId);

        return new ModelMapper();
    }
}
