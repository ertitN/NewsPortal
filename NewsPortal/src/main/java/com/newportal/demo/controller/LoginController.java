package com.newportal.demo.controller;

import com.newportal.demo.dto.LoginDTO;
import com.newportal.demo.dto.UserDTO;
import com.newportal.demo.entity.User;
import com.newportal.demo.mapper.UserDTOMapper;
import com.newportal.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    public UserDTOMapper userDTOMapper;



    @PostMapping("/register")
    public ResponseEntity<UserDTO> saveUser(@RequestBody User user){
      UserDTO userDTO = userDTOMapper.userToUserDTO(userService.saveUser(user));
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        userService.validateUser(loginDTO);
        return new ResponseEntity<>("Login Successful!",HttpStatus.OK);
    }









}
