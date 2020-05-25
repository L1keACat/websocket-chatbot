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

    public void deleteAll(String username) {
        messageRepository.deleteAll(findByUsername(username));
    }

    public List<Message> findByUsername(String username) {
        return messageRepository.findByUserName(username, Sort.by(Sort.Direction.DESC, "created"));
    }

    public Message findLastUserMes(String username) {
        if (findByUsername(username).size() > 3)
            return findByUsername(username).get(findByUsername(username).size()-3);
        else
            return null;
    }

    public Message findLastBotMes(String username) {
        if (findByUsername(username).size() > 2)
            return findByUsername(username).get(findByUsername(username).size()-2);
        else
            return null;
    }

    public Message findPreLastUserMes(String username) {
        if (findByUsername(username).size() > 5)
            return findByUsername(username).get(findByUsername(username).size()-5);
        else
            return null;
    }

    public Message findPreLastBotMes(String username) {
        if (findByUsername(username).size() > 4)
            return findByUsername(username).get(findByUsername(username).size()-4);
        else
            return null;
    }
}
