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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import static com.backend.service.utils.Security.*;

public class SecretService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecretRepository secretRepository;

    @Autowired
    private Environment environment;

    public Secret create(String userId, Secret newSecret) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
//        TODO : setup cofiguration with the keys to ensure the information is correctly implemented

//        Make sure the given userId matches the userId form the JWT token
        newSecret.setUserId(userId);

//        Generate and store the iv associated with the user
        newSecret.setInitializatonVector(generateIv());

//        Encrypt the content of the secret
        newSecret.setContent(
            encrypt(environment.getProperty("aes.algorithm"), newSecret.getContent(), generateKeyFromPassword(environment.getProperty("aes.key"), userId), newSecret.getInitializatonVector())
        );

        secretRepository.save(newSecret);

        return newSecret;
    }

    public List<Secret> getAllEncryptedUsersSecretList(String userId) {

        List<Secret> allSecretsOfUser = secretRepository.findByUserId(userId);

//       Remove all encrypted content from the payload
        for (Secret currentSecret : allSecretsOfUser ) {
            currentSecret.setContent(null);
        }

        return allSecretsOfUser;
    }

    public Secret getDecryptedSecret(String userId, String id)
            throws
            InvalidKeySpecException,
            NoSuchAlgorithmException,
            IllegalBlockSizeException,
            InvalidKeyException,
            BadPaddingException,
            InvalidAlgorithmParameterException,
            NoSuchPaddingException, ForbiddenAccess {
        Secret secret = secretRepository.findById(id).get();
        if (secret.getUserId() != userId) {
            throw new ForbiddenAccess("User does not own the secret");
        }

//        Decrypt the secret
        secret.setContent(
                decrypt("AES/CBC/PKCS5Padding", secret.getContent(), generateKeyFromPassword(environment.getProperty("aes.key"), userId), secret.getInitializatonVector())
        );

        return secret;
    }

    public Secret updateSecret(String userId, String id, Secret newSecret)
            throws
            ForbiddenAccess,
            InvalidKeySpecException,
            NoSuchAlgorithmException,
            IllegalBlockSizeException,
            InvalidKeyException,
            BadPaddingException,
            InvalidAlgorithmParameterException,
            NoSuchPaddingException {
        Secret secret = secretRepository.findById(id).get();
        if (secret.getUserId() != userId) {
            throw new ForbiddenAccess("User does not own the secret");
        }
//        Encrypt the new contents and assign them to the already existing secret
        secret.setContent(
                encrypt(environment.getProperty("aes.algorithm"), newSecret.getContent(), generateKeyFromPassword(environment.getProperty("aes.key"), userId), newSecret.getInitializatonVector())
        );

        secretRepository.save(secret);

        return secret;
    }

    public boolean deleteSecret(String userId, String id) throws ForbiddenAccess {
        Secret secret = secretRepository.findById(id).get();
        if (secret.getUserId() != userId) {
            throw new ForbiddenAccess("User does not own the secret");
        }

        secretRepository.deleteById(id);

//        Envoie de la confirmation de la suppression du secret
        if (!secretRepository.findById(id).isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteAllUserSecrets(String userId) {
        List<Secret> allSecretsOfUser = secretRepository.findByUserId(userId);
        for (Secret currentSecret : allSecretsOfUser) {
            secretRepository.deleteById(currentSecret.getId());
        }

        if (secretRepository.findByUserId(userId).isEmpty()) {
            return true;
        }

        return false;
    }
}
















