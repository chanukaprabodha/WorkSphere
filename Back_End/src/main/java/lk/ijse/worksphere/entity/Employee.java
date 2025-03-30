package lk.ijse.worksphere.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private String position;
    private BigDecimal salary;
    private String profilePicture;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
