package lk.ijse.worksphere.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 10:42 AM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    private String id;
    private LocalDate date;
    private LocalTime inTime;
    private LocalTime outTime;
    private Time totalHours;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    public enum Status {
        PRESENT, ABSENT, HALF_DAY, LEAVE
    }
}
