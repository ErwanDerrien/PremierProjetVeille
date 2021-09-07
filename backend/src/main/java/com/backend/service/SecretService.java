package com.backend.service;

import com.backend.exception.ForbiddenAccess;
import com.backend.model.Secret;
import com.backend.model.User;
import com.backend.repository.SecretRepository;
import com.backend.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import static com.backend.service.utils.Security.*;

public class SecretService {

    @Autowired
    private SecretRepository secretRepository;

    @Autowired
    private Environment environment;

    private static String secretPassword = null;
    private static String aesAlgorithm = null;

//    TODO : fractionner les fonctions

    private String getSecretPassword() {
        if(secretPassword == null) {
            secretPassword = environment.getProperty("aes.password");
        }
        return secretPassword;
    }

    private String getAESAlgorithm() {
        if(aesAlgorithm == null) {
            aesAlgorithm = environment.getProperty("aes.algorithm");
        }
        return aesAlgorithm;
    }

    public Secret create(String userId, Secret newSecret) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        return create(userId,newSecret, getAESAlgorithm(), getSecretPassword());

    }

    public Secret create(String userId, Secret newSecret, String aesAlgorithm, String secretPassword) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

//        Make sure the given userId matches the userId form the JWT token
        newSecret.setUserId(userId);

//        Generate and store the iv associated with the user
        newSecret.setInitializatonVector(generateIv());

//        Encrypt the content of the secret
        newSecret.setContent(
            encrypt(aesAlgorithm, newSecret.getContent(), generateKeyFromPassword(secretPassword, userId), newSecret.getInitializatonVector())
        );

        secretRepository.save(newSecret);

        return newSecret;
    }

    public List<Secret> getAllEncryptedUsersSecretList(String userId) {

        List<Secret> allSecretsOfUser = secretRepository.findByUserId(userId);

//       Remove all encrypted content from the payload
        for (Secret currentSecret : allSecretsOfUser ) {
            currentSecret.setContent(null);
            currentSecret.setInitializatonVector((null));
        }

        return allSecretsOfUser;
    }

    public Secret getSecretFromDb(String userId, String secretId) throws ForbiddenAccess {
        // Get secret
        Secret secret = secretRepository.findById(secretId).get();
        // Verify owner
        if (secret.getUserId() != userId) {
            throw new ForbiddenAccess("User does not own the secret");
        }
        // Return
        return secret;
    }

    public Secret saveSecretInDb(Secret newSecret) throws ForbiddenAccess {
        // Get secret
        Secret secret = secretRepository.findById(newSecret.getId()).get();
        // Verify owner
        if (secret.getUserId() != newSecret.getUserId()) {
            throw new ForbiddenAccess("User does not own the secret");
        }
        secretRepository.save(secret);
        return secret;
    }

    public String decryptContent(String content, String algorithm, String secretPassword, String salt, IvParameterSpec iv) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        return decrypt(algorithm, content, generateKeyFromPassword(secretPassword, salt), iv);
    }

    public String encryptContent(String content, String algorithm, String secretPassword, String salt, IvParameterSpec iv) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        return encrypt(algorithm, content, generateKeyFromPassword(secretPassword, salt), iv);
    }

    public Secret get(String userId, String secretId, boolean decrupt) throws ForbiddenAccess, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        // Get secret
        Secret secret = getSecretFromDb(userId,secretId);

        // Optional desryption
        if(decrupt) {
            secret.setContent(
                    decryptContent(secret.getContent(), getAESAlgorithm(), getSecretPassword(), secret.getUserId(), secret.getInitializatonVector())
            );
        }

        // Hide the Initialization Vector
        secret.setInitializatonVector((null));

        // Return
        return secret;
    }

    public Secret update(String userId, Secret newSecret) throws ForbiddenAccess, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        // Get secret
        Secret secret = getSecretFromDb(userId, newSecret.getId());

        if (!newSecret.getName().isEmpty()) {
            secret.setName(newSecret.getName());
        }

        if (!newSecret.getContent().isEmpty()) {
            secret.setContent(
                    encryptContent(secret.getContent(), getAESAlgorithm(), getSecretPassword(), secret.getUserId(), secret.getInitializatonVector())
            );
        }

        saveSecretInDb(secret);

        return secret;
    }

    public void deleteSecret(String userId, String id) throws ForbiddenAccess {
        // Get secret
        Secret secret = getSecretFromDb(userId, id);
        if (secret.getUserId() != userId) {
            throw new ForbiddenAccess("User does not own the secret");
        }

        secretRepository.deleteById(id);
    }

    public int deleteAllUserSecrets(String userId) {
        List<Secret> allSecretsOfUser = secretRepository.findByUserId(userId);
        for (Secret currentSecret : allSecretsOfUser) {
            secretRepository.deleteById(currentSecret.getId());
        }
        return allSecretsOfUser.size();
    }

}
















