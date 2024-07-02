package com.newportal.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newportal.demo.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String userName;
    private String lastName;
    private List<RoleDTO> roles;
}
