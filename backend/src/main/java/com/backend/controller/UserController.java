package com.backend.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.backend.exception.AlreadyExists;
import com.backend.exception.DoesntExist;
import com.backend.exception.InvalidPassword;
import com.backend.model.User;
import com.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {
        try {
            String userId = userService.create(user);

            UriComponents uriComponent = uriComponentsBuilder.path("/api/v1/user/{id}").buildAndExpand(userId);

            return ResponseEntity.status(201).location(uriComponent.toUri()).build();

        } catch (AlreadyExists e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(("User already exists"));
        } catch (InvalidPassword e) {
            return ResponseEntity.status(400).body("Password does not meet the quality criteria");
        } catch (Exception e) {
            return ResponseEntity.status(503).body(("Unexpected error"));
        }
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    public ResponseEntity<User> get(@PathVariable String userId, Principal loggedUser) {

        if (!loggedUser.getName().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            User user = userService.get(userId);

            return ResponseEntity.status(200).body(user);

        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<Void> modifyUserPassword(@RequestBody User user, @PathVariable String userId,
            Principal loggedUser) {

        if (!loggedUser.getName().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            userService.modifyUserPassword(userId, user.getPassword());
            return ResponseEntity.status(200).build();

        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        } catch (InvalidPassword e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(503).build();
        }
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId, Principal loggedUser) {

        if (!loggedUser.getName().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            userService.delete(userId);
            return ResponseEntity.status(200).build();

        } catch (DoesntExist doesntExist) {
            return ResponseEntity.status(404).build();
        }
    }

}