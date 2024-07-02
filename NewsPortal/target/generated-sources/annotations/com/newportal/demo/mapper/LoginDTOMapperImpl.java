package com.newportal.demo.mapper;

import com.newportal.demo.dto.LoginDTO;
import com.newportal.demo.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T20:06:29+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
@Component
public class LoginDTOMapperImpl implements LoginDTOMapper {

    @Override
    public LoginDTO userToLoginDTO(User user) {
        if ( user == null ) {
            return null;
        }

        LoginDTO loginDTO = new LoginDTO();

        loginDTO.setUserName( user.getUserName() );
        loginDTO.setPassword( user.getPassword() );

        return loginDTO;
    }

    @Override
    public User loginDTOToUser(LoginDTO loginDTO) {
        if ( loginDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUserName( loginDTO.getUserName() );
        user.setPassword( loginDTO.getPassword() );

        return user;
    }
}
