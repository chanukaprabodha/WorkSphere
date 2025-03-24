package lk.ijse.worksphere.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 12:39 PM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String action;

    @Timestamp
    private LocalDateTime CreatedAt;
}
