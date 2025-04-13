package lk.ijse.worksphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String nic;
    private String position;
    private BigDecimal salary;
    private String profilePicture;
    private String departmentId;
    private int annualLeaves;
    private int casualLeave;
    private int sickLeave;
    private int leaveBalance;
}
