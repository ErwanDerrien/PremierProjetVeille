package com.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.backend.exception.DoesntExist;
import com.backend.model.User;
import com.backend.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void testCreate() {

    }

    @Test
    void testDelete() {

    }

    @Test
    void testSuccessfulGet() throws DoesntExist {
        User testUser = new User();

        when(userRepository.getById("testId")).thenReturn(testUser);

        User retrievedUser = userService.get("testId");

        assertEquals(testUser, retrievedUser);
    }

    @Test
    void testUnsuccessfulGet() {

        when(userRepository.getById("testId")).thenReturn(null);

        Assertions.assertThrows(DoesntExist.class, () -> {
            userService.get("testId");
        });

    }

    @Test
    void testModifyUserPassword() {

    }
}
