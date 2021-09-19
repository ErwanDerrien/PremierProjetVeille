package com.backend.controller.security;

import com.backend.controller.security.util.JwtTokenUtil;
import com.backend.model.User;
import com.backend.service.UserPrincipalService;
import com.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
// @RequestMapping("/api/v1/authenticate")
public class AuthentificationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/authenticate/login")
    public ResponseEntity<String> createAuthentificationToken(@RequestBody User user) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(),
                    userService.preparePassword(user.getId(), user.getPassword())));
        } catch (DisabledException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final UserDetails userDetails = userPrincipalService.loadUserByUsername(user.getId());

        final String token = tokenUtil.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_PLAIN).body(token);

    }
}
