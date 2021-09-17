package com.backend.service;

import com.backend.exception.DoesntExist;
import com.backend.exception.ForbiddenAccess;
import com.backend.model.Secret;
import com.backend.repository.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.backend.service.utils.Security.*;

@Service
public class SecretService {

    @Autowired
    private SecretRepository secretRepository;

    @Value("${aes.password}")
    protected String secretPassword;

    @Value("${aes.algorithm}")
    private String aesAlgorithm;

    protected List<Byte> generateSeed() {
        final int seedSize = 16;
        byte[] preSeed = new byte[seedSize];
        new SecureRandom().nextBytes(preSeed);

        List<Byte> seed = new ArrayList<>();
        for (int i = 0; i < seedSize; i++) {
            seed.add(preSeed[i]);
        }
        return seed;
    }

    public static IvParameterSpec generateIv(List<Byte> seed) {
        byte[] iv = new byte[seed.size()];
        for (int i = 0; i < seed.size(); i++) {
            iv[i] = seed.get(i);
        }
        return new IvParameterSpec(iv);
    }

    public String create(String userId, Secret newSecret)
            throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        return create(userId, newSecret, aesAlgorithm, secretPassword);

    }

    public String create(String userId, Secret newSecret, String aesAlgorithm, String secretPassword)
            throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        userId = userId.toLowerCase();

        // Generate new Id
        newSecret.setId(UUID.randomUUID().toString());

        // Make sure the given userId matches the userId form the JWT token
        newSecret.setUserId(userId);

        // Generate and store the seed associated with the secret
        List<Byte> seed = generateSeed();
        newSecret.setSeed(seed);

        // Generate the iv with the secret's seed
        IvParameterSpec ivParameterSpec = generateIv(seed);

        // Encrypt the content of the secret
        newSecret.setContent(encrypt(aesAlgorithm, newSecret.getContent(),
                generateKeyFromPassword(secretPassword, userId), ivParameterSpec));

        secretRepository.save(newSecret);

        return newSecret.getId();
    }

    public List<Secret> getAllEncryptedUsersSecretList(String userId) {
        userId = userId.toLowerCase();

        List<Secret> allSecretsOfUser = secretRepository.findByUserId(userId);

        // Return empty array if no corresponding secrets
        if (allSecretsOfUser == null) {
            return new ArrayList<Secret>();
        }

        // Remove all encrypted content from the payload
        for (Secret currentSecret : allSecretsOfUser) {
            currentSecret.setContent(null);
            currentSecret.setSeed((null));
        }

        return allSecretsOfUser;
    }

    public Secret getSecretFromDb(String secretId, String userId) throws DoesntExist {
        userId = userId.toLowerCase();

        // Get secret
        Secret existingSecret = secretRepository.getByIds(secretId, userId);
        if (existingSecret == null) {
            throw new DoesntExist("secretId given does not correspond to any stored secret");
        }

        // Return
        return existingSecret;
    }

    public Secret saveUpdatedSecretInDb(Secret newSecret, String userId) throws ForbiddenAccess, DoesntExist {
        userId = userId.toLowerCase();

        // Verify secret's presence in repository
        Secret existingSecret = secretRepository.getByIds(newSecret.getId(), userId);
        if (existingSecret == null) {
            throw new DoesntExist("Given secret id does not correspond to any secret in repository");
        }

        // Verify owner
        if (!existingSecret.getUserId().equals(newSecret.getUserId())) {
            throw new ForbiddenAccess("User does not own the secret");
        }

        secretRepository.save(newSecret);
        return newSecret;
    }

    public String decryptContent(String content, String algorithm, String secretPassword, String salt,
            IvParameterSpec iv) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println(content);
        System.out.println(algorithm);
        System.out.println(secretPassword);
        System.out.println(salt);
        System.out.println(generateKeyFromPassword(secretPassword, salt));
        System.out.println(iv);

        return decrypt(algorithm, content, generateKeyFromPassword(secretPassword, salt), iv);
    }

    public String encryptContent(String content, String algorithm, String secretPassword, String salt,
            IvParameterSpec iv) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        return encrypt(algorithm, content, generateKeyFromPassword(secretPassword, salt), iv);
    }

    public Secret get(String secretId, String userId, boolean decrypt) throws ForbiddenAccess, BadPaddingException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException,
            NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, DoesntExist {
        userId = userId.toLowerCase();

        // Get secret
        Secret secret = getSecretFromDb(secretId, userId);

        // Optional desryption
        if (decrypt) {
            // Generate the iv with the secret's seed
            IvParameterSpec ivParameterSpec = generateIv(secret.getSeed());
            secret.setContent(decryptContent(secret.getContent(), this.aesAlgorithm, this.secretPassword,
                    secret.getUserId(), ivParameterSpec));
        } else {
            secret.setContent(null);
        }

        // Hide the seed
        secret.setSeed((null));

        // Return
        return secret;
    }

    public Secret update(Secret newSecret, String userId) throws ForbiddenAccess, BadPaddingException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException,
            NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, DoesntExist {
        userId = userId.toLowerCase();

        // Get secret
        Secret secret = getSecretFromDb(newSecret.getId(), userId);

        if (!newSecret.getName().isEmpty()) {
            secret.setName(newSecret.getName());
        }

        if (!newSecret.getContent().isEmpty()) {
            // Generate the iv with the secret's seed
            IvParameterSpec ivParameterSpec = generateIv(secret.getSeed());
            secret.setContent(encryptContent(secret.getContent(), aesAlgorithm, secretPassword, secret.getUserId(),
                    ivParameterSpec));
        }

        saveUpdatedSecretInDb(secret, userId);

        return secret;
    }

    public void delete(String id, String userId) throws ForbiddenAccess, DoesntExist {
        userId = userId.toLowerCase();

        // Get secret
        Secret secret = getSecretFromDb(id, userId);
        if (!secret.getUserId().equals(userId)) {
            throw new ForbiddenAccess("User does not own the secret");
        }

        secretRepository.deleteByIds(id, userId);
    }

    public int deleteAllUserSecrets(String userId) {
        userId = userId.toLowerCase();

        List<Secret> allSecretsOfUser = secretRepository.findByUserId(userId);
        for (Secret currentSecret : allSecretsOfUser) {
            secretRepository.deleteByIds(currentSecret.getId(), userId);
        }
        return allSecretsOfUser.size();
    }

}
