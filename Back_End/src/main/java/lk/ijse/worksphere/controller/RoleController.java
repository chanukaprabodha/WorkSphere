package lk.ijse.worksphere.controller;

import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.entity.Roles;
import lk.ijse.worksphere.util.VarList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllRoles() {
        List<String> roles = Arrays.stream(Roles.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", roles));
    }
}
