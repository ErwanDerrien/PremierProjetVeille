package com.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.model.UserPrincipal;

@Service
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return new UserPrincipal(userService.get(username));
        }
        catch(Exception e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
