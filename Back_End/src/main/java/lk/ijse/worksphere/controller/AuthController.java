package lk.ijse.worksphere.controller;


import lk.ijse.worksphere.dto.AuthDTO;
import lk.ijse.worksphere.dto.ResponseDTO;
import lk.ijse.worksphere.dto.UserDTO;
import lk.ijse.worksphere.service.impl.UserServiceImpl;
import lk.ijse.worksphere.util.JwtUtil;
import lk.ijse.worksphere.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final ResponseDTO responseDTO;

    //constructor injection
    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userService, ResponseDTO responseDTO) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.responseDTO = responseDTO;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO) {
        System.out.println("Authenticating user controller" + userDTO);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        } catch (Exception e) {
            // Log the exception for debugging purposes
            System.out.println(userDTO.getEmail() + " " + userDTO.getPassword());
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Invalid Credentials", e.getMessage()));
        }

        UserDTO loadedUser = userService.loadUserDetailsByUsername(userDTO.getEmail());
        if (loadedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        String token = jwtUtil.generateToken(loadedUser);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(loadedUser.getEmail());
        authDTO.setToken(token);
        authDTO.setRoles(loadedUser.getRoles());
        System.out.println("AuthController line 64 : " + authDTO.getRoles());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(VarList.Created, "Success", authDTO));
    }

}

