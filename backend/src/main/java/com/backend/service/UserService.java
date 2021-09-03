package com.backend.service;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.backend.exception.DoesntExist;
import com.backend.exception.MissingParameter;
import com.backend.model.User;
import com.backend.repository.SecretRepository;
import com.backend.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.rmi.ServerError;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

@Service
@ComponentScan(basePackages = {"com.backend.repository"})
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User inscription(final User user) throws ServerError, MissingParameter {
        // TODO
        // Verification de la validite de l'adresse courriel
        // Setup la vérification avec l'api de pwned –Nice to have–

        if (user.getId().isEmpty() || user.getPassword().isEmpty()) {
            throw new MissingParameter("Missing at least a parameter for the given user");
        }

        // Verify that the email address that was given has no uppercase characters
        user.setId(user.getId().toLowerCase(Locale.ROOT));

        // Hash the password with the id
        user.setPassword(hashPassword(user.getPassword(), user.getId()));

        // Verify that there isn't already an other user with the same email address
        // TODO : ask why do i explode an exception simply if i put an adress i already put in, would'nt that break my application
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
//            throw new AlreadyExistsException("User already exist");
            System.out.println("User already exists");
        }

        User createdUser = userRepository.save(user);

        return createdUser;
    }

    public User connexion(String id, String password) throws DoesntExist, ServerError {
        // Verify that there's already a user with the same email address
        Optional<User> existingUser = userRepository.findById(id);
        if (!existingUser.isPresent()) {
//            throw new DoesntExist("User does not exist");
            System.out.println("User does not exist");
        }

        // Verify that the passwords match
        String encodedPassword = hashPassword(password, id);
        if (existingUser.get().getPassword().equals(encodedPassword)) {

        }

        return null;
    }

    public void modifyUserPassword(String id, String password) throws ServerError {
        Optional<User> modifiedUser = userRepository.findById(id);
        modifiedUser.get().setPassword(hashPassword(modifiedUser.get().getPassword(), modifiedUser.get().getId()));

        userRepository.save(modifiedUser.get());
    }

    public String hashPassword(String stringToHash, String salt) throws ServerError {
        final MessageDigest digest;
        try {
            String idPassword = stringToHash + salt;
            digest = MessageDigest.getInstance("SHA3-256");
            final byte[] hashbytes = digest.digest(
                    idPassword.getBytes(StandardCharsets.UTF_8)
            );

            String hashedPassword = bytesToHex(hashbytes);

//            byte[] decodedBytes = Base64.getDecoder().decode(hashbytes);
//            String hashedPassword = new String(decodedBytes);

            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServerError("Connot process password", null);
        }
    }

    // https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
