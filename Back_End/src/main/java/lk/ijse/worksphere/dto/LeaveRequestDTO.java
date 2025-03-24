package lk.ijse.worksphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 01:21 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeaveRequestDTO {
    private String id;
    private String employeeId;
    private String startDate;
    private String endDate;
    private String reason;
    private String status;
}
