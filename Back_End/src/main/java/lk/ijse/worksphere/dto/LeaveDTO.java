package lk.ijse.worksphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 01:21 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaveDTO {
    private String id;
    private String employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
    private LeaveType leaveType;
    private int leaveDays;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public enum LeaveType {
        SICK,
        CASUAL,
        ANNUAL
    }
}
