package com.newportal.demo.service;

import com.newportal.demo.dao.RoleDAO;
import com.newportal.demo.dao.UserDAO;
import com.newportal.demo.entity.User;
import com.newportal.demo.config.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);
    }

    public static CustomUserDetails customUserDetails() {
        return(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}
