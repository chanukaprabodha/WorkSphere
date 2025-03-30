package lk.ijse.worksphere.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.worksphere.dto.UserDTO;
import lk.ijse.worksphere.entity.User;
import lk.ijse.worksphere.repository.UserRepo;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private ModelMapper modelMapper;

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
        System.out.println(authorities);
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
        String generatedId;
        do {
            generatedId = IdGenerator.generateId("USR-");
        } while (userRepo.existsById(generatedId));
        userDTO.setId(generatedId);
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            System.out.println("password : " + userDTO.getPassword());
            userRepo.save(modelMapper.map(userDTO, User.class));
            return VarList.Created;
        }
    }
}
