package com.newportal.demo.controller;

import com.newportal.demo.dto.UserDTO;
import com.newportal.demo.entity.User;
import com.newportal.demo.mapper.UserDTOMapper;
import com.newportal.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    private UserDTOMapper userDTOMapper;

    @Autowired
    public UserController(UserService userService, UserDTOMapper userDTOMapper) {
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
    }



    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        UserDTO dto = userDTOMapper.userToUserDTO(userService.findUserById(id));
        return new ResponseEntity<>(dto,HttpStatus.OK);

    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user, Principal principal, @PathVariable Long id){
       User updatedUser =  userService.updateUser(user,id,principal.getName());

       UserDTO dto =  userDTOMapper.userToUserDTO(updatedUser);
       return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "User is deleted!";

    }

}
