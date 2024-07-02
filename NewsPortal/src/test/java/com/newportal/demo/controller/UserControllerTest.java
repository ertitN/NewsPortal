package com.newportal.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newportal.demo.entity.Role;
import com.newportal.demo.entity.User;
import com.newportal.demo.exception.UserNotFoundExc;
import com.newportal.demo.service.UserService;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.security.Principal;
import java.util.Arrays;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    UserService userServiceMock;

    @Autowired
    MockMvc mockMvc;

    Role role;
    User user;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    public void setup(){
        role = new Role("ROLE_AUTHOR");

        user = new User(1L, "ertitn", "test123", "Nedim", "Ertit",
                Arrays.asList(role));

    }

    @Test
    @DisplayName("Successfull Http Test For Finding User By Id")
    public void httpTestForFindUserById() throws Exception {
        when(userServiceMock.findUserById(anyLong())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.firstName",is("Nedim")))
                .andExpect(jsonPath("$.lastName",is("Ertit")))
                .andExpect(jsonPath("$.userName",is("ertitn")));

    }

    @Test
    @DisplayName("Failed Http Test For Finding User By Id When User Is Not Exists")
    public void httpTestForFindingUserByIdWhenUserIsNotExists() throws Exception {
        UserNotFoundExc userNotFoundExc = new UserNotFoundExc("User with this id is not exists");

        when(userServiceMock.findUserById(anyLong())).thenThrow(userNotFoundExc);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}",5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }


    @Test
    @DisplayName("Successful Http Test For Updating User")
    @WithMockUser(username = "johndoe",password = "doe123",roles = "AUTHOR")
    public void httpTestForUpdatingUser() throws Exception {
        User tempUser = new User(1L, "johndoe", "doe123", "John", "Doe",
                Arrays.asList(role));


        when(userServiceMock.updateUser(ArgumentMatchers.any(User.class),anyLong(),anyString()))
                .thenReturn(tempUser);


        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.userName",is("johndoe")))
                .andExpect(jsonPath("$.firstName",is("John")))
                .andExpect(jsonPath("$.lastName",is("Doe")));


    }

    @Test
    @DisplayName("Failed Http Test For User When User Is Not Authenticated")
    public void httpTestForUpdatingUserWhenUserIsNotAuthorized() throws Exception{

        User tempUser = new User(1L, "johndoe", "doe123", "John", "Doe",
                Arrays.asList(role));


        when(userServiceMock.updateUser(ArgumentMatchers.any(User.class),anyLong(),anyString()))
                .thenThrow(RuntimeException.class);


        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempUser)))
                .andExpect(status().is4xxClientError());

    }


    @Test
    @DisplayName("Successful Test For Deleting User")
    @WithMockUser(username = "admin",password = "test123",roles = "ADMIN")
    public void httpTestForDeletingUser() throws Exception {
        when(userServiceMock.deleteUser(anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}",5))
                .andExpect(status().isOk())
                .andExpect(content().string("User is deleted!"));


    }

    @Test
    @DisplayName("Failed Test For Deleting User When User Is Not Exists")
    @WithMockUser(username = "admin",password = "test123",roles = "ADMIN")
    public void httpTestForDeletingUserWhenUserIsNotExists() throws Exception{

        when(userServiceMock.deleteUser(anyLong())).thenThrow(UserNotFoundExc.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}",5))
                .andExpect(status().is4xxClientError());


    }



}
