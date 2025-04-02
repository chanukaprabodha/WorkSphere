package lk.ijse.worksphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 01:20 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceDTO {
    private String id;
    private LocalDate date;
    private String employeeId;
    private LocalTime inTime;
    private LocalTime outTime;
    private String status;
}
