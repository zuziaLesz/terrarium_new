package com.example.smartTerrarium;

import com.example.smartTerrarium.dto.CreateUserDto;
import com.example.smartTerrarium.dto.UserLoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUser() throws Exception {
        CreateUserDto dto = new CreateUserDto(
                "becia",
                "becia@wp.pl",
                "dobrykot222"
        );

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldLoginUser() throws Exception {

        CreateUserDto dto = new CreateUserDto(
                "becia",
                "becia@wp.pl",
                "dobrykot222"
        );

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        UserLoginData userLoginData = new UserLoginData(
                "becia@wp.pl",
                "dobrykot222"
        );
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldReturnNotExistIfWrongEmail() throws Exception {
        CreateUserDto dto = new CreateUserDto(
                "becia",
                "becia@wp.pl",
                "dobrykot222"
        );

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        UserLoginData userLoginData = new UserLoginData(
                "beata@wp.pl",
                "dobrykot222"
        );
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginData)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnWrongCredentialsIfWrongPassword() throws Exception {
        CreateUserDto dto = new CreateUserDto(
                "becia",
                "becia@wp.pl",
                "dobrykot222"
        );

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        UserLoginData userLoginData = new UserLoginData(
                "be@wp.pl",
                "zlykot222"
        );
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginData)))
                .andExpect(status().isUnauthorized());
    }
}

