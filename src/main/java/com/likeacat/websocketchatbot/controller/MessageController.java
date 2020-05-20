package com.likeacat.websocketchatbot.controller;

import com.likeacat.websocketchatbot.entities.Message;
import com.likeacat.websocketchatbot.repositories.MessageRepository;
import com.likeacat.websocketchatbot.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class MessageController {

    private final MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/messages/{username}")
    public List<Message> getMessages(@PathVariable String username) {
        return messageService.findByUsername(username);
    }

    @PostMapping("/messages")
    void addMessage(@RequestBody Message message) {
        messageRepository.save(message);
    }
}
