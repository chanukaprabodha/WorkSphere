package lk.ijse.worksphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 02:26 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class ResponseDTO {
    private int code;
    private String Message;
    private Object data;
}
