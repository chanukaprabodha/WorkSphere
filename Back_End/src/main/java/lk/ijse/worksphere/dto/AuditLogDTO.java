package lk.ijse.worksphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 01:59 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuditLogDTO {
    private String id;
    private String userId;
    private String action;
}
