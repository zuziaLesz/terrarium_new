package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.CreateUserDto;
import com.example.smartTerrarium.dto.UserLoginData;
import com.example.smartTerrarium.entity.User;
import com.example.smartTerrarium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User createDefaultUser(){
        User user = User.builder()
                .id(1)
                .name("zuzia")
                .email("zuzia")
                .password("zuzia")
                .build();
        return userRepository.save(user);
    }

    public void registerUser(CreateUserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
    }

    public User login(UserLoginData loginData) {
        User user = getUserByEmail(loginData.getEmail());
        if(user.getPassword() == loginData.getPassword()) {
            return user;
        }
        else throw new RuntimeException();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
