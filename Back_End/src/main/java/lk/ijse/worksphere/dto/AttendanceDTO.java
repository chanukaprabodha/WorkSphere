package lk.ijse.worksphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String employeeId;
    private String inTime;
    private String outTime;
}
