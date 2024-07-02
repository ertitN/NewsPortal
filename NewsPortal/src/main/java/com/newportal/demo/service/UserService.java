package com.newportal.demo.service;


import com.newportal.demo.dao.RoleDAO;
import com.newportal.demo.dao.UserDAO;

import com.newportal.demo.dto.LoginDTO;
import com.newportal.demo.entity.Role;
import com.newportal.demo.entity.User;
import com.newportal.demo.exception.UserNameAlreadyExistsExc;
import com.newportal.demo.exception.UserNotFoundExc;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;


    @Transactional
    public User saveUser(User user){

        if (userDAO.findByUserName(user.getUserName())!=null) {
            throw new UserNameAlreadyExistsExc("User with this username: "+user.getUserName()+" is already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserName(user.getUserName());
        Role role = roleDAO.findByName("ROLE_AUTHOR");

        if (role==null) {
            role = checkRolExist();
        }

        user.setRoles(Arrays.asList(role));
        return userDAO.save(user);
    }

    @Transactional
    public User updateUser(User user,Long id,String userName){

        User tempUser = findUserById(id);

        if (!tempUser.getUserName().equals(userName)){
            throw new AccessDeniedException("Access Denied!");

        }
        tempUser.setFirstName(user.getFirstName());
        tempUser.setLastName(user.getLastName());
        if(user.getPassword()!=null & user.getUserName()!=null){
            tempUser.setUserName(user.getUserName());
            tempUser.setPassword(passwordEncoder.encode(user.getPassword()));

        }



        return userDAO.save(tempUser);

    }


    public User findUserById(long id){

        Optional<User> user = userDAO.findById(id);
        if(user.isEmpty()){
            throw new UserNotFoundExc("User with "+id+" doesn't exist!");
        }

        return user.get();


    }


    @Transactional
    public Boolean deleteUser(long id){
        User user = findUserById(id);
        user.setRoles(null);
        userDAO.delete(user);
        return true;

    }



    private Role checkRolExist(){
        Role role = new Role();
        role.setName("ROLE_AUTHOR");
        return roleDAO.save(role);
    }


    public boolean validateUser(LoginDTO loginDTO) {

        boolean isPasswordMatches = passwordEncoder.matches(
                loginDTO.getPassword(),userDAO.getHashedPasswordByUserName(loginDTO.getUserName()));

        boolean isValidate = userDAO.existsByUserName(loginDTO.getUserName())
                                                && isPasswordMatches;




        if (!isValidate) {
            throw new UserNotFoundExc("Username Or Password is wrong !");
        }

        return isValidate;

    }
}
