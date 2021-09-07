package com.backend.controller;

import com.backend.exception.DoesntExist;
import com.backend.exception.MissingParameter;
import com.backend.model.User;
import com.backend.service.UserService;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.rmi.ServerError;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public String create(@RequestBody User user, HttpServletResponse response) {
        try {
            return userService.create(user);
        } catch (ServerError serverError) {
            serverError.printStackTrace();
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        } catch (MissingParameter missingParameter) {
            missingParameter.printStackTrace();
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return null;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(@RequestBody User user) {
        try {
            userService.login(user.getId(), user.getPassword());
        } catch (DoesntExist doesntExist) {
            doesntExist.printStackTrace();
        } catch (ServerError serverError) {
            serverError.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/modifyUserPassword", method = RequestMethod.POST)
    public boolean modifyUserPassword(@RequestBody User user) {
        try {
            userService.modifyUserPassword(user.getId(), user.getPassword());
            return true;
        } catch (ServerError serverError) {
            serverError.printStackTrace();
        }
        return false;
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable("userId") String userId) {
        userService.delete(userId);
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    public User get(@PathVariable("userId") String userId) {
        return userService.get(userId);
    }
}







