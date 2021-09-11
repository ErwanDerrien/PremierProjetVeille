package com.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.exception.AlreadyExists;
import com.backend.exception.DoesntExist;
import com.backend.exception.InvalidPassword;
import com.backend.exception.MissingParameter;
import com.backend.model.User;
import com.backend.repository.UserRepository;

import java.util.Locale;

@Service
public class UserService {

    // TODO: verifier si mettre le `userId` en lettres minuscules est toujours intéressant...

    @Autowired
    private UserRepository userRepository;

    public User get(String userId) throws DoesntExist {
        User existingUser = userRepository.getById(userId);
        if (existingUser == null) {
            throw new DoesntExist("User already exist");
        }
        return existingUser;
    }

    private String preparePassword(String password) throws InvalidPassword {
        // Check password quality
        if (password == null) {
            throw new InvalidPassword("Quality checks not met! Cannot be null...");
        }
        if (password.length() < 8) {
            throw new InvalidPassword("Quality checks not met! Cannot be so short...");
        }

        // TODO: add more quality checks

        return new BCryptPasswordEncoder().encode(password);
    }

    public String create(final User user) throws MissingParameter, AlreadyExists, InvalidPassword {
        // TODO : Verification de la validite de l'adresse courriel
        // TODO : Setup la vérification avec l'api de pwned –Nice to have–

        if (user.getId() == null || user.getId().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new MissingParameter("Missing at least a parameter for the given user");
        }

        // Verify that the email address that was given has no uppercase characters
        user.setId(user.getId().toLowerCase(Locale.ROOT));

        // Verify that there isn't already an other user with the same email address
        User existingUser = userRepository.getById(user.getId());
        if (existingUser != null) {
            throw new AlreadyExists("User already exist");
        }

        // Hash the password with the id
        user.setPassword(preparePassword(user.getPassword()));

        userRepository.save(user);

        return user.getId();
    }

    public void modifyUserPassword(String userId, String password) throws DoesntExist, InvalidPassword {
        User existingUser = userRepository.getById(userId);
        if (existingUser == null) {
            throw new DoesntExist("User already exist");
        }

        existingUser.setPassword(preparePassword(password));
        userRepository.save(existingUser);
    }

    public void delete(String userId) throws DoesntExist {
        User existingUser = userRepository.getById(userId);
        if (existingUser == null) {
            throw new DoesntExist("User already exist");
        }

        userRepository.deleteById(userId);
    }
}
