package lk.ijse.worksphere.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-11
 * Time: 09:24 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String address;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;
    @Column(unique = true, nullable = false)
    private String nic;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String position;
    private BigDecimal salary;
    private String profilePicture;
    private int annualLeaves;
    private int casualLeave;
    private int sickLeave;
    private int leaveBalance;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
