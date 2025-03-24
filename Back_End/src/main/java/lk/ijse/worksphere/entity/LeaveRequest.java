package lk.ijse.worksphere.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 10:51 AM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "leave_request")
public class LeaveRequest {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Timestamp
    private LocalDateTime createdAt;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}
