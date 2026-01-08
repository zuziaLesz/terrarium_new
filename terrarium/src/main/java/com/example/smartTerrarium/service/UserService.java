package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.CreateUserDto;
import com.example.smartTerrarium.dto.UserEditDto;
import com.example.smartTerrarium.dto.UserLoginData;
import com.example.smartTerrarium.dto.UserSendData;
import com.example.smartTerrarium.entity.User;
import com.example.smartTerrarium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User registerUser(CreateUserDto userDto) {
        // check if email already taken
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Authenticate user and return a JWT token if credentials are valid.
     * @return JWT token string
     */
    public UserSendData login(UserLoginData loginData) {
        User user = getUserByEmail(loginData.getEmail());
        if(passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
           String token = jwtService.generateToken(user.getEmail());
           return UserSendData.builder()
                   .email(user.getEmail())
                   .name(user.getName())
                   .token(token)
                   .build();
        }
        else throw new RuntimeException("Invalid credentials");
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
    public UserEditDto editUser(UserEditDto userEditDto, Integer id) {
        User user = userRepository.getUserById(id);
        if(userEditDto.getEmail() != null) {
            user.setEmail(userEditDto.getEmail());
        }
        else userEditDto.setEmail(user.getEmail());
        if(userEditDto.getName() != null) {
            user.setName(userEditDto.getName());
        }
        else userEditDto.setName(user.getName());
        userRepository.save(user);
        return userEditDto;
    }
}
