package com.backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backend.exception.AlreadyExists;
import com.backend.exception.DoesntExist;
import com.backend.exception.InvalidPassword;
import com.backend.exception.MissingParameter;
import com.backend.model.User;
import com.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
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

    private User testUser;
    final String id = "testId";

    @BeforeEach
    public void prepareUser() {
        testUser = new User();
        testUser.setId(id);
        testUser.setPassword("password");
    }

    @Test
    void testSuccessfulCreate() throws DoesntExist, MissingParameter, AlreadyExists, InvalidPassword {
        when(userRepository.getById(id.toLowerCase())).thenReturn(null);

        userService.create(testUser);

        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUnsuccessfulCreate() throws DoesntExist, MissingParameter, AlreadyExists, InvalidPassword {
        when(userRepository.getById(id.toLowerCase())).thenReturn(testUser);

        assertThrows(AlreadyExists.class, () -> {
            userService.create(testUser);
        });
    }

    @Test
    void testSuccessfulDelete() throws DoesntExist, MissingParameter, AlreadyExists, InvalidPassword {
        when(userRepository.getById(id.toLowerCase())).thenReturn(testUser);

        userService.delete(testUser.getId());

        verify(userRepository, times(1)).deleteById(testUser.getId());
    }

    @Test
    void testUnsuccessfulDelete() {
        when(userRepository.getById(id.toLowerCase())).thenReturn(null);

        assertThrows(DoesntExist.class, () -> {
            userService.delete(testUser.getId());
        });
    }

    @Test
    void testSuccessfulGet() throws DoesntExist {
        when(userRepository.getById(id.toLowerCase())).thenReturn(testUser);

        User retrievedUser = userService.get(id);

        assertEquals(testUser, retrievedUser);
    }

    @Test
    void testUnsuccessfulGet() {

        when(userRepository.getById(id.toLowerCase())).thenReturn(null);

        assertThrows(DoesntExist.class, () -> {
            userService.get(id);
        });

    }

    @Test
    void testSuccessfulModifyUserPassword() throws DoesntExist, InvalidPassword {

        when(userRepository.getById(id.toLowerCase())).thenReturn(testUser);

        String originalHashedPassword = userService.get(id).getPassword();
        userService.modifyUserPassword(id, "newPassword");
        String newHashedPassword = userService.get(id).getPassword();

        assertNotEquals(originalHashedPassword, newHashedPassword);
    }

    @Test
    void testUnsuccessfulModifyUserPassword() throws DoesntExist, InvalidPassword {

        when(userRepository.getById(id.toLowerCase())).thenReturn(testUser);

        String originalHashedPassword = userService.get(id).getPassword();
        userService.modifyUserPassword(id, "newPassword");
        String newHashedPassword = userService.get(id).getPassword();

        assertNotEquals(originalHashedPassword, newHashedPassword);
    }
}
