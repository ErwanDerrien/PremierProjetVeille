package com.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.backend.exception.AlreadyExists;
import com.backend.exception.DoesntExist;
import com.backend.exception.InvalidPassword;
import com.backend.exception.MissingParameter;
import com.backend.model.User;
import com.backend.service.UserService;

// @WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    Principal loggedUser;

    @BeforeEach
    void setUp() {
        loggedUser = mock(Principal.class);
    }

    @Test
    void testSuccessfulCreate()
            throws URISyntaxException, DoesntExist, MissingParameter, AlreadyExists, InvalidPassword {
        URI uri = new URI("http://localhost/");

        UriComponents uriComponent = mock(UriComponents.class);
        when(uriComponent.toUri()).thenReturn(uri);

        UriComponentsBuilder uriComponentsBuilder = mock(UriComponentsBuilder.class);
        when(uriComponentsBuilder.path("/api/v1/user/{id}")).thenReturn(uriComponentsBuilder);
        when(uriComponentsBuilder.buildAndExpand("1")).thenReturn(uriComponent);

        User user = new User();
        when(userService.create(user)).thenReturn("1");

        ResponseEntity<?> responseEntity = userController.create(user, uriComponentsBuilder);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(uri, responseEntity.getHeaders().getLocation());

        verify(userService, times(1)).create(user);
    }

    @Test
    void testCreateFailsForExistingUser()
            throws URISyntaxException, DoesntExist, MissingParameter, AlreadyExists, InvalidPassword {
        User user = new User();
        when(userService.create(user)).thenThrow(AlreadyExists.class);

        ResponseEntity<?> responseEntity = userController.create(user, null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(userService, times(1)).create(user);
    }

    @Test
    void testCreateFailsForInvalidPassword()
            throws URISyntaxException, DoesntExist, MissingParameter, AlreadyExists, InvalidPassword {
        User user = new User();
        when(userService.create(user)).thenThrow(InvalidPassword.class);

        ResponseEntity<?> responseEntity = userController.create(user, null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(userService, times(1)).create(user);
    }

    @Disabled
    @Test
    void testCreateFailsForUnexpected()
            throws URISyntaxException, DoesntExist, MissingParameter, AlreadyExists, InvalidPassword {
        User user = new User();
        when(userService.create(user)).thenThrow(IllegalArgumentException.class);

        ResponseEntity<?> responseEntity = userController.create(user, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verify(userService, times(1)).create(user);
    }

    @Test
    void testSuccessfulDelete() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");
        ResponseEntity<?> responseEntity = userController.delete(loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService, times(1)).delete(loggedUser.getName());
    }

    @Test
    void testDeleteForDoesnotExist() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");
        doThrow(DoesntExist.class).when(userService).delete(any());

        ResponseEntity<?> responseEntity = userController.delete(loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(userService, times(1)).delete(loggedUser.getName());
    }

    @Test
    void testDeleteForWrongId() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");

        ResponseEntity<?> responseEntity = userController.delete("whatever", loggedUser);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(userService, never()).delete(any());
    }

    @Disabled
    @Test
    void testDeleteForUnexpectedError() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");
        doThrow(IllegalArgumentException.class).when(userService).delete(any());

        ResponseEntity<?> responseEntity = userController.delete(loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verify(userService, times(1)).delete(loggedUser.getName());
    }

    @Test
    void testSuccessfulGet() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");
        ResponseEntity<?> responseEntity = userController.get(loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService, times(1)).get(loggedUser.getName());
    }

    @Test
    void testGetForDoesnotExist() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");
        doThrow(DoesntExist.class).when(userService).get(any());

        ResponseEntity<?> responseEntity = userController.get(loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(userService, times(1)).get(loggedUser.getName());
    }

    @Disabled
    @Test
    void testGetForWrongId() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");

        ResponseEntity<?> responseEntity = userController.get("whatever", loggedUser);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(userService, never()).get(any());
    }

    @Disabled
    @Test
    void testGetForUnexpectedError() throws DoesntExist {
        when(loggedUser.getName()).thenReturn("lU");
        doThrow(IllegalArgumentException.class).when(userService).get(any());

        ResponseEntity<?> responseEntity = userController.get(loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verify(userService, times(1)).get(loggedUser.getName());
    }

    @Test
    void testSuccessfulModifyPassword() throws DoesntExist, InvalidPassword {
        User user = new User();
        user.setPassword("psw");
        when(loggedUser.getName()).thenReturn("lU");
        ResponseEntity<?> responseEntity = userController.modifyUserPassword(user, loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService, times(1)).modifyUserPassword(loggedUser.getName(), user.getPassword());
    }

    @Test
    void testModifyPasswordForDoesnotExist() throws DoesntExist, InvalidPassword {
        User user = new User();
        user.setPassword("psw");
        when(loggedUser.getName()).thenReturn("lU");
        doThrow(DoesntExist.class).when(userService).modifyUserPassword(any(), any());

        ResponseEntity<?> responseEntity = userController.modifyUserPassword(user, loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        verify(userService, times(1)).modifyUserPassword(loggedUser.getName(), user.getPassword());
    }

    @Test
    void testModifyPasswordForWrongId() throws DoesntExist, InvalidPassword {
        User user = new User();
        user.setPassword("psw");
        when(loggedUser.getName()).thenReturn("lU");

        ResponseEntity<?> responseEntity = userController.modifyUserPassword(user, "whatever", loggedUser);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(userService, never()).modifyUserPassword(any(), any());
    }

    @Test
    void testModifyPasswordForInvalidPassword() throws DoesntExist, InvalidPassword {
        User user = new User();
        user.setPassword("psw");
        when(loggedUser.getName()).thenReturn("lU");
        doThrow(InvalidPassword.class).when(userService).modifyUserPassword(any(), any());

        ResponseEntity<?> responseEntity = userController.modifyUserPassword(user, loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(userService, times(1)).modifyUserPassword(loggedUser.getName(), user.getPassword());
    }

    @Disabled
    @Test
    void testModifyPasswordForUnexpectedError() throws DoesntExist, InvalidPassword {
        User user = new User();
        user.setPassword("psw");
        when(loggedUser.getName()).thenReturn("lU");
        doThrow(IllegalArgumentException.class).when(userService).modifyUserPassword(any(), any());

        ResponseEntity<?> responseEntity = userController.modifyUserPassword(user, loggedUser.getName(), loggedUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verify(userService, times(1)).modifyUserPassword(loggedUser.getName(), user.getPassword());
    }
}
