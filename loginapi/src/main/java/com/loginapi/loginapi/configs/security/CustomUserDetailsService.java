package com.loginapi.loginapi.configs.security;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.loginapi.loginapi.model.UserModel;
import com.loginapi.loginapi.services.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel model = this.service.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new User(model.getEmail(), model.getPassword(), new ArrayList<>());
    }
    

}
