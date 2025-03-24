package lk.ijse.worksphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 02:00 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {
    private String id;
    private String userId;
    private String message;
}
