package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.*;
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
    public ResponseEntity<User> createDefaultUser (@RequestBody com.example.smartTerrarium.dto.CreateUserDto userDto) {
        User user = userService.registerUser(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserDto userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserSendData> login(@RequestBody UserLoginData loginData) {
        return ResponseEntity.ok(userService.login(loginData));
    }
    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<Void> changePassword(@PathVariable int userId, @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto, userId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<NewUserDataDto> editUser(@RequestBody UserEditDto userDto, @PathVariable Integer id) {
        return ResponseEntity.ok(userService.editUser(userDto, id));
    }
}
