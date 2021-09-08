package com.backend.controller;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.backend.exception.DoesntExist;
import com.backend.exception.MissingParameter;
import com.backend.model.User;
import com.backend.service.UserService;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.rmi.ServerError;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody User user, HttpServletResponse response, UriComponentsBuilder uriComponentsBuilder) {
        try {
            String userId = userService.create(user);

            UriComponents uriComponent = uriComponentsBuilder.path("/api/v1/user/{id}").buildAndExpand(userId);

            return ResponseEntity.status(201).location(
                    uriComponent.toUri()
            ).build();
        } catch (ServerError | AlreadyExistsException | MissingParameter serverError) {
            System.out.println("Unexpected error in UserController.create()");
            return ResponseEntity.status(503).body(("Unexpected error"));
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@RequestBody User user) {
        try {
            userService.login(user.getId(), user.getPassword());
            return ResponseEntity.status(200).build();
        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        } catch (ServerError serverError) {
            return ResponseEntity.status(503).build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PutMapping()
    public ResponseEntity<Void> modifyUserPassword(@RequestBody User user, @RequestParam String userId) {
        try {
            userService.modifyUserPassword(userId, user.getPassword());
            return ResponseEntity.status(200).build();

        } catch (ServerError serverError) {
            return ResponseEntity.status(503).build();
        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam String userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.status(200).build();
        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<User> get(@RequestParam("userId") String userId, UriComponentsBuilder uriComponentsBuilder) {
        try {
            User user = userService.get(userId);

            UriComponents uriComponent = uriComponentsBuilder.path("/api/v1/user/{id}").buildAndExpand(userId);

            return ResponseEntity.status(200).location(
                    uriComponent.toUri()
            ).body(user);
        } catch (DoesntExist doesntExist) {
            doesntExist.printStackTrace();
            return ResponseEntity.status(404).build();
        }
    }
}







