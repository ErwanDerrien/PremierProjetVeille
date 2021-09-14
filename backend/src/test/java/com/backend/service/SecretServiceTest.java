package com.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.backend.exception.DoesntExist;
import com.backend.exception.ForbiddenAccess;
import com.backend.model.Secret;
import com.backend.model.User;
import com.backend.repository.SecretRepository;
import com.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SecretServiceTest {

    @InjectMocks
    SecretService secretService;

    @Mock
    SecretRepository secretRepository;

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private User testUser;
    final String userTestId = "userTestId";

    private Secret testSecret;
    final String secretTestId = "secretTestId";

    @BeforeEach
    public void prepareUser() {
        testUser = new User();
        testUser.setId(userTestId);
        testUser.setPassword("password");
    }

    @BeforeEach
    public void prepareSecret() {
        testSecret = new Secret();
        testSecret.setId(secretTestId);
        testSecret.setUserId(userTestId);
        testSecret.setName("name");
        testSecret.setContent("content");
    }

    @Test
    void testCreate() {

    }

    @Test
    void testCreate2() {

    }

    @Test
    void testDecryptContent() {

    }

    @Test
    void testDelete() {

    }

    @Test
    void testDeleteAllUserSecrets() {

    }

    @Test
    void testEncryptContent() {

    }

    @Test
    void testGenerateIv() {

    }

    @Test
    void testSuccessfulGet() throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeySpecException,
            ForbiddenAccess, DoesntExist {
        when(userRepository.getById(userTestId.toLowerCase())).thenReturn(testUser);
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(testSecret);

        Secret retrievedSecret = secretService.get(secretTestId, userTestId, true);

        assertEquals(testSecret, retrievedSecret);
    }

    @Test
    void testGetAllEncryptedUsersSecretList() {

    }

    @Test
    void testGetSecretFromDb() {

    }

    @Test
    void testSaveUpdatedSecretInDb() {

    }

    @Test
    void testUpdate() {

    }
}
