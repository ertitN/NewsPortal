package com.newportal.demo.mapper;

import com.newportal.demo.dto.LoginDTO;
import com.newportal.demo.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginDTOMapper {
    LoginDTO userToLoginDTO(User user);
    User loginDTOToUser(LoginDTO loginDTO);
}
