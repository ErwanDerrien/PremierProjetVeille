package com.backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

// @ActiveProfiles("test")
// @SpringBootTest(properties = "aes.algorithm=AES/CBC/PKCS5Padding")
// @SpringBootTest(properties = "aes.password=Rd19InoZ9IhIXkgjc0Kk")
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

    final String wrongUserId = "wrongUserId";

    private Secret testSecret;
    final String secretTestId = "secretTestId";

    private Secret secondTestSecret;
    final String secondSecretTestId = "secondSecretTestId";

    final List<Secret> secretsList = new ArrayList<>();

    @BeforeEach
    public void prepareUser() {
        testUser = new User();
        testUser.setId(userTestId);
        testUser.setPassword("password");
    }

    @BeforeEach
    public void prepareSecrets() {
        testSecret = new Secret();
        testSecret.setId(secretTestId);
        testSecret.setUserId(userTestId.toLowerCase());
        testSecret.setName("name");
        testSecret.setContent("content");
        testSecret.setSeed(secretService.generateSeed());

        secondTestSecret = new Secret();
        secondTestSecret.setId(secondSecretTestId);
        secondTestSecret.setUserId(userTestId.toLowerCase());
        secondTestSecret.setName("secondName");
        secondTestSecret.setContent("secondContent");
        secondTestSecret.setSeed(secretService.generateSeed());

        secretsList.add(testSecret);
        secretsList.add(secondTestSecret);
    }

    @BeforeEach
    public void setUpProperties() {
        ReflectionTestUtils.setField(secretService, "secretPassword", "WDXeY5hjIUKk8SRwxfc88q4izxgamkpUjnj83KcU");
        ReflectionTestUtils.setField(secretService, "aesAlgorithm", "AES/CBC/PKCS5Padding");
    }

    @Test
    void testCreate() {

    }

    @Test
    void testDecryptContent() {

    }

    @Test
    void testDelete() throws ForbiddenAccess, DoesntExist {
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(testSecret);

        secretService.delete(secretTestId, userTestId);

        verify(secretRepository, times(1)).deleteByIds(testSecret.getId(), userTestId.toLowerCase());
    }

    @Test
    void testDeleteAllUserSecrets() {

    }

    @Test
    void testEncryptContent() {

    }

    @Test
    void testSuccessfulGetEncrypted() throws InvalidKeyException, BadPaddingException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException,
            NoSuchPaddingException, InvalidKeySpecException, ForbiddenAccess, DoesntExist {
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(testSecret);

        Secret retrievedSecret = secretService.get(secretTestId, userTestId, false);

        assertEquals(testSecret, retrievedSecret);
    }

    @Disabled
    @Test
    void testSuccessfulGetDecrypted() throws InvalidKeyException, BadPaddingException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException,
            NoSuchPaddingException, InvalidKeySpecException, ForbiddenAccess, DoesntExist {

        when(userRepository.getById(userTestId.toLowerCase())).thenReturn(testUser);
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(testSecret);

        Secret retrievedSecret = secretService.get(secretTestId, userTestId, true);

        assertEquals(testSecret, retrievedSecret);
    }

    @Test
    void testSuccesGetAllEncryptedUsersSecretList() {
        when(secretRepository.findByUserId(userTestId.toLowerCase())).thenReturn(secretsList);

        assertEquals(2, secretService.getAllEncryptedUsersSecretList(userTestId).size());
    }

    @Test
    void testFailGetAllEncryptedUsersSecretList() {
        when(secretRepository.findByUserId(userTestId.toLowerCase())).thenReturn(null);

        assertEquals(0, secretService.getAllEncryptedUsersSecretList(userTestId).size());
    }

    @Test
    void testSuccessGetSecretFromDb() throws DoesntExist {
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(testSecret);

        assertEquals(testSecret, secretService.getSecretFromDb(secretTestId, userTestId));
    }

    @Test
    void testFailGetSecretFromDbDoesntExist() throws DoesntExist {
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(null);

        assertThrows(DoesntExist.class, () -> {
            secretService.getSecretFromDb(secretTestId, userTestId);
        });
    }

    @Test
    void testSaveUpdatedSecretInDb() throws ForbiddenAccess, DoesntExist, InvalidKeyException, BadPaddingException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException,
            NoSuchPaddingException, InvalidKeySpecException {
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(testSecret);

        Secret origialSecret = secretService.getSecretFromDb(secretTestId, userTestId);
        String originalContent = origialSecret.getContent();
        origialSecret.setContent("newContent");
        secretService.saveUpdatedSecretInDb(origialSecret, userTestId);
        Secret updatedSecret = secretService.getSecretFromDb(secretTestId, userTestId);
        String updatedContent = updatedSecret.getContent();

        assertNotEquals(originalContent, updatedContent);
    }

    @Test
    void testUpdate() throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeySpecException,
            ForbiddenAccess, DoesntExist {
        when(secretRepository.getByIds(secretTestId, userTestId.toLowerCase())).thenReturn(testSecret);

        Secret origialSecret = secretService.getSecretFromDb(secretTestId, userTestId);
        String originalContent = origialSecret.getContent();
        origialSecret.setContent("newContent");
        secretService.update(origialSecret, userTestId);
        Secret updatedSecret = secretService.getSecretFromDb(secretTestId, userTestId);
        String updatedContent = updatedSecret.getContent();

        assertNotEquals(originalContent, updatedContent);
    }
}
