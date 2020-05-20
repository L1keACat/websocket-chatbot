package com.likeacat.websocketchatbot.services;

import com.likeacat.websocketchatbot.entities.Message;
import com.likeacat.websocketchatbot.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void add(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    public List<Message> findByUsername(String username) {
        return messageRepository.findByUserName(username, Sort.by(Sort.Direction.DESC, "created"));
    }

    public Message findPreLast(String username) {
        return findByUsername(username).get(findByUsername(username).size()-3);
    }

    public Message findLast(String username) {
        return findByUsername(username).get(findByUsername(username).size()-2);
    }
}
