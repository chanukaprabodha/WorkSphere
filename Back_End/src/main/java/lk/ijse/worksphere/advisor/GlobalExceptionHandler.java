package lk.ijse.worksphere.advisor;

import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 02:24 PM
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ResponseDTO> handelMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        for (FieldError fieldError: ex.getBindingResult().getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ResponseDTO responseDTO = new ResponseDTO(
                VarList.Unprocessable_Entity,
                "Validation Error",
                errors
        );
        return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
    }
}
