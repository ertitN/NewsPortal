package com.newportal.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newportal.demo.dto.LoginDTO;
import com.newportal.demo.entity.Role;
import com.newportal.demo.entity.User;
import com.newportal.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import static org.hamcrest.Matchers.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    User user;


    @BeforeEach
    public void setup(){

        user = new User("user","pass");
    }

    @Test
    @DisplayName("Successfull Test For Sign Up")
    public void httpTestForSignUp() throws Exception{
        when(userService.saveUser(ArgumentMatchers.any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName",is("user")));

    }

    @Test
    @DisplayName("Successfull Test For Sign In")
    public void httpTestForSignIn(){

        when(userService.validateUser(ArgumentMatchers.any(LoginDTO.class))).thenReturn(true);
    }
}
