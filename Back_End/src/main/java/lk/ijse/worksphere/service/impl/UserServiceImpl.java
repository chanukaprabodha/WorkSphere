package lk.ijse.worksphere.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.worksphere.dto.UserDTO;
import lk.ijse.worksphere.entity.Employee;
import lk.ijse.worksphere.entity.User;
import lk.ijse.worksphere.repository.EmployeeRepo;
import lk.ijse.worksphere.repository.UserRepo;
import lk.ijse.worksphere.service.EmailService;
import lk.ijse.worksphere.service.UserService;
import lk.ijse.worksphere.util.IdGenerator;
import lk.ijse.worksphere.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-19
 * Time: 09:35 AM
 */

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        return modelMapper.map(user, UserDTO.class);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles().toString()));
        return authorities;
    }

    @Override
    public UserDTO searchUser(String username) {
        if (userRepo.existsByEmail(username)) {
            User user = userRepo.findByEmail(username);
            return modelMapper.map(user, UserDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public int saveUser(UserDTO userDTO) {
        Employee employee = employeeRepo.findById(userDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String generatedId;
        do {
            generatedId = IdGenerator.generateId("USR");
        } while (userRepo.existsById(generatedId));
        userDTO.setId(generatedId);
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        } else {
            String passwordBeforeEncode = userDTO.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepo.save(modelMapper.map(userDTO, User.class));

            String message = getMessage(passwordBeforeEncode, employee);

            emailService.sendHtmlEmail(employee.getEmail(), "System Access Credentials", message);

            return VarList.Created;
        }
    }

    private static String getMessage(String Password, Employee employee) {
        String message = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;">
                <h2 style="text-align: center; color: #343a40;">System Account Created</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Your system account has been successfully created. Please find your login credentials below:</p>
            
                <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;">
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>Password:</strong> %s</p>
                </div>
            
                <p><strong>Please log in and change your password immediately for security purposes.</strong></p>
            
                <hr style="margin: 30px 0;">
                <p style="font-size: 14px; color: #6c757d;">This is an automated message from WorkSphere. If you did not expect this, please contact your administrator.</p>
            </div>
            """, employee.getFirstName(), employee.getEmail(), Password);
        return message;
    }
}
