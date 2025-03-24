package lk.ijse.worksphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 12:53 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phone;
    private String nic;
    private String position;
    private BigDecimal salary;
    private String profilePicture;
    private String departmentId;
    private String roleId;
}
