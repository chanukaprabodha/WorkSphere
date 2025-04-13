package lk.ijse.worksphere.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "`leave`")
public class Leave {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private int days;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Type leaveType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    public enum Type {
        SICK, CASUAL, ANNUAL
    }

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = Status.PENDING;
        }
        if (this.leaveType == null) {
            this.leaveType = Type.ANNUAL;
        }
    }

}
