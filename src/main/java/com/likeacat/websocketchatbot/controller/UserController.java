package com.likeacat.websocketchatbot.controller;

import com.likeacat.websocketchatbot.entities.User;
import com.likeacat.websocketchatbot.repositories.UserRepository;
import com.likeacat.websocketchatbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;
    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByName(username);
    }

    @PostMapping("/users")
    void addUser(@RequestBody User user) {
        userRepository.save(user);
    }
}
