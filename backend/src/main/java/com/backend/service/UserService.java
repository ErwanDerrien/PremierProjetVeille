package com.backend.service;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.backend.exception.DoesntExist;
import com.backend.exception.MissingParameter;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.rmi.ServerError;
import java.util.*;

import static com.backend.service.utils.Security.hashPassword;
import static com.backend.service.utils.Security.hashStrings;

@Service
@ComponentScan(basePackages = {"com.backend.repository"})
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(final User user) throws ServerError, MissingParameter {
        // TODO
        // Verification de la validite de l'adresse courriel
        // Setup la vérification avec l'api de pwned –Nice to have–

        if (user.getId().isEmpty() || user.getPassword().isEmpty()) {
            throw new MissingParameter("Missing at least a parameter for the given user");
        }

        // Verify that the email address that was given has no uppercase characters
        user.setId(user.getId().toLowerCase(Locale.ROOT));

        // Verify that there isn't already an other user with the same email address
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            throw new AlreadyExistsException("User already exist");
        }

        // Hash the password with the id
        List<String> stringsToHash = new ArrayList<>();
        stringsToHash.add(user.getPassword());
        stringsToHash.add(user.getId());

        user.setPassword(hashStrings(stringsToHash));

        User createdUser = userRepository.save(user);

        return createdUser;
    }

    public User connexion(String id, String password) throws DoesntExist, ServerError, AuthenticationException {

        // Verify that the email address that was given has no uppercase characters
        id = id.toLowerCase(Locale.ROOT);

        // Verify that there's already a user with the same email address
        Optional<User> existingUser = userRepository.findById(id);
        if (!existingUser.isPresent()) {
            throw new DoesntExist("Wrong credentials");
        }

        // Verify that the passwords match
        List<String> listTmp = new ArrayList<>();
        listTmp.add(password);
        listTmp.add(id);
        String encodedPassword = hashStrings(listTmp);
        if (!existingUser.get().getPassword().equals(encodedPassword)) {
            throw new AuthenticationException("Wrong credentials");
        }

        return existingUser.get();
    }

    public boolean modifyUserPassword(String id, String password) throws ServerError {
        Optional<User> modifiedUser = userRepository.findById(id);
        modifiedUser.get().setPassword(hashPassword(modifiedUser.get().getPassword(), modifiedUser.get().getId()));

        userRepository.save(modifiedUser.get());

        return true;
    }

    public boolean deleteUser(String userId) {
        userRepository.deleteById(userId);

        if (userRepository.findById(userId).isEmpty()) {
            return true;
        }

        return false;
    }
}
