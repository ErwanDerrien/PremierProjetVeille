package com.backend.service;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.backend.model.User;
import com.backend.repository.SecretRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Optional;

@Service
@ComponentScan(basePackages = {"com.backend.repository"})
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User inscription(final User user)  {
        // TODO
        // Verification de la présence du email et du mdp
        // Verification de la validite de l'adresse courriel
        // Setup la vérification avec l'api de pwned –Nice to have–

        // verify that the email address that was given has no uppercase characters
        user.setId(user.getId().toLowerCase(Locale.ROOT));

        //hash the password with the id
        hashPassword(user);

        //verify that there isn't already an other user with the same email address
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            throw new AlreadyExistsException("Already exist");
        }

        User createdUser = userRepository.save(user);

        return createdUser;
    }

    public void modifyUserPassword(User user, String password) {
        Optional<User> modifiedUser = userRepository.findById(user.getId());
        modifiedUser.get().setPassword(password);
        hashPassword(modifiedUser.get());
        userRepository.save(modifiedUser.get());
    }

    public void hashPassword(User user) {
        final MessageDigest digest;
        try {
            String idPassword = user.getId() + user.getPassword();
            digest = MessageDigest.getInstance("SHA3-256");
            final byte[] hashbytes = digest.digest(
                    idPassword.getBytes(StandardCharsets.UTF_8)
            );
            String hashedPassword = bytesToHex(hashbytes);
            user.setPassword(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
