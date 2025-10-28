package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.CreateUserDto;
import com.example.smartTerrarium.dto.UserLoginData;
import com.example.smartTerrarium.entity.User;
import com.example.smartTerrarium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createDefaultUser")
    public ResponseEntity<User> createDefaultUser () {
        User user = userService.createDefaultUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/login")
//    public ResponseEntity<User> login(@RequestBody UserLoginData loginData) {
//        return ResponseEntity.ok(userService.login(loginData));
//    }

}
