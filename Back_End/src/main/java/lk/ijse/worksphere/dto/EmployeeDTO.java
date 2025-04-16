package lk.ijse.worksphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "First name should contain only letters and spaces")
    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name should contain only letters and spaces")
    @Size(min = 2, max = 50, message = "Last name should be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address should be between 5 and 255 characters")
    private String address;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+94|0)\\d{9}$", message = "Phone number should start with +94 or 0 and have 9 digits")
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank(message = "NIC is required")
    @Pattern(
            regexp = "^\\d{9}[Vv]$|^\\d{12}$",
            message = "NIC should be 9 digits followed by 'V' or 12 digits"
    )
    private String nic;

    @NotBlank(message = "Position is required")
    @Size(min = 2, max = 100, message = "Position should be between 2 and 100 characters")
    private String position;

    private String salary;

    private String departmentId;
    private int annualLeaves;
    private int casualLeave;
    private int sickLeave;
    private int leaveBalance;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

}
