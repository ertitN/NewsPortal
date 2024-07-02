package com.newportal.demo.mapper;

import com.newportal.demo.dto.UserDTO;
import com.newportal.demo.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public
interface  UserDTOMapper {


     public UserDTO userToUserDTO(User user);


    public User userDTOtoUser(UserDTO userDTO);

}
