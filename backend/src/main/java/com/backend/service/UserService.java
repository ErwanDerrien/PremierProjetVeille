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

    public String create(final User user) throws ServerError, MissingParameter, AlreadyExistsException {
        // TODO : Verification de la validite de l'adresse courriel
        // TODO : Setup la vérification avec l'api de pwned –Nice to have–

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

        return createdUser.getId();
    }

    public void login(String id, String password) throws DoesntExist, ServerError, AuthenticationException {

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
    }

    public void modifyUserPassword(String id, String password) throws ServerError, DoesntExist {
        Optional<User> modifiedUser = userRepository.findById(id);
        if (modifiedUser.isEmpty()) {
            throw new DoesntExist("Given userId doesn't belong to any stored user");
        }
        modifiedUser.get().setPassword(hashPassword(password, id));
        userRepository.save(modifiedUser.get());
    }

    public void delete(String userId) throws DoesntExist {
        if (userRepository.findById(userId).isEmpty()) {
            throw new DoesntExist("Given userId doesn't belong to any stored user");
        }
        userRepository.deleteById(userId);
//        TODO : expire JWT token because user does not exist anymore
//        TODO : chain delete all secrets associated with the user (frontend or backend?)
    }

    public User get(String userId) throws DoesntExist {
        if (!userRepository.findById(userId).isPresent()) {
            throw new DoesntExist("Given userId doesn't belong to any stored user");
        }
        return userRepository.findById(userId).get();
    }
}
