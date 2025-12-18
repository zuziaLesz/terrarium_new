package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.CreateUserDto;
import com.example.smartTerrarium.dto.UserLoginData;
import com.example.smartTerrarium.entity.User;
import com.example.smartTerrarium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createDefaultUser")
    public ResponseEntity<User> createDefaultUser (@RequestBody com.example.smartTerrarium.dto.CreateUserDto userDto) {
        // Create default user using provided credentials (name/email/password)
        User user = userService.registerUser(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginData loginData) {
        String token = userService.login(loginData);
        return ResponseEntity.ok(token);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
