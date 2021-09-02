package com.backend;

import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User newUser = new User();
        newUser.setId("erwan1derrien@gmail.com");
        newUser.setPassword("test");
        userService.inscription(newUser);
        System.out.println("mdp avant : " + userRepository.findById("erwan1derrien@gmail.com").get().getPassword());
        userService.modifyUserPassword(newUser, "deuxieme test");
        System.out.println("mdp apres : " + userRepository.findById("erwan1derrien@gmail.com").get().getPassword());
    }
}
