package com.newportal.demo.service;

import com.newportal.demo.dao.RoleDAO;
import com.newportal.demo.dao.UserDAO;
import com.newportal.demo.entity.Role;
import com.newportal.demo.entity.User;
import com.newportal.demo.exception.UserNotFoundExc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserServiceTest {

    @Mock
    UserDAO mockedUserDao;

    @Mock
    RoleDAO roleDAO;

    Role role;

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    String encryptedPass;

    User user;


    @BeforeEach
    public void setup(){
        user = new User(1L, "test", "test123", "Nedim", "Ertit", Arrays.asList(
                new Role("AUTHOR")
        ));

        encryptedPass = "$2a$12$/tDMsyWH63eg/A9PLHMqhusNdlNEY4LwgcRS.QHVq8SvWJZ.F13Ra";
    }


    @Test
    @DisplayName("Successfull Test For Saving User")
    public void testForSaveUser(){


        when(roleDAO.findByName("ROLE_AUTHOR")).thenReturn(user.getRoles().get(0));
        when(mockedUserDao.save(user)).thenReturn(user);

       assertEquals(user.getId(),userService.saveUser(user).getId());

    }


    @Test
    @DisplayName("Test For Updating User")
    public void testForUpdatingUser(){
        User updatedUser = new User(1L, "test", "test123", "John", "Doe", Arrays.asList(
                new Role("AUTHOR")
        ));

        when(mockedUserDao.findById(1L)).thenReturn(Optional.of(user));
        when(mockedUserDao.save(any())).thenReturn(updatedUser);

        assertEquals("John",userService.updateUser(user,1L,"test").getFirstName());
    }

    @Test
    @DisplayName("Failed Test For Updating User When User Not Exists")
    public void testForUserNotExists(){
        User updatedUser = new User(1L, "test", "test123", "John", "Doe", Arrays.asList(
                new Role("AUTHOR")
        ));
        when(mockedUserDao.findById(1L)).thenThrow(UserNotFoundExc.class);
        when(mockedUserDao.save(any())).thenReturn(updatedUser);

        assertThrows(UserNotFoundExc.class,()->{
            userService.updateUser(user,1L,"test");
        });


    }

    @Test
    @DisplayName("Successful Test For Find User By Id")
    public void testForFindUserById(){
        when(mockedUserDao.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(user.getId(),userService.findUserById(1L).getId());

    }

    @Test
    @DisplayName("Failed Test For Finding User By Id When User Is Not Exists")
    public void failedTestForFindingUserById(){
        when(mockedUserDao.findById(1L)).thenThrow(UserNotFoundExc.class);

        assertThrows(UserNotFoundExc.class,()->{
            userService.findUserById(1L);
        });
    }


    @Test
    @DisplayName("Successful Test For Deleting User By Id")
    public void testForDeletingUserById(){
        when(mockedUserDao.findById(anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        verify(mockedUserDao,times(1)).delete(user);
    }

    @Test
    @DisplayName("Failed Test For Deleting User When User Not Found")
    public void testForDeletingUserByIdWhenUserNotFound(){
        when(mockedUserDao.findById(anyLong())).thenThrow(UserNotFoundExc.class);
        assertThrows(UserNotFoundExc.class,()->{
            userService.deleteUser(2L);
        });


    }




}
