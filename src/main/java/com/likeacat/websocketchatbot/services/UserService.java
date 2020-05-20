package com.likeacat.websocketchatbot.services;

import com.likeacat.websocketchatbot.entities.User;
import com.likeacat.websocketchatbot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void add(User user) {
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void remove(String name) {
        User user = userRepository.findByName(name);
        userRepository.delete(user);
    }
}
