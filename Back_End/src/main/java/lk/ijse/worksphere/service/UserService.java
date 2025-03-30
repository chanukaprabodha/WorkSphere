package lk.ijse.worksphere.service;

import lk.ijse.worksphere.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-19
 * Time: 09:36 AM
 */

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);

    UserDetails loadUserByUsername(String email);
}
