package com.likeacat.websocketchatbot.controller;

import com.likeacat.websocketchatbot.bot.bot;
import com.likeacat.websocketchatbot.entities.Message;
import com.likeacat.websocketchatbot.entities.User;
import com.likeacat.websocketchatbot.services.MessageService;
import com.likeacat.websocketchatbot.services.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@Controller
@CrossOrigin("http://localhost:4200")
public class ChatController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate template;

    bot bot = new bot();

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message chatMessage) throws IOException, JSONException {
        messageService.add(chatMessage);
        template.convertAndSend("/topic/public/" + chatMessage.getSender(), chatMessage);

        Message botAnswer = new Message();
        botAnswer.setContent(bot.answer(chatMessage.getContent(), chatMessage.getSender(), messageService, userService));
        botAnswer.setSender("Bot");
        botAnswer.setType(Message.MessageType.CHAT);
        botAnswer.setUserName(chatMessage.getSender());
        messageService.add(botAnswer);
        template.convertAndSend("/topic/public/" + chatMessage.getSender(), botAnswer);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload Message chatMessage,
                        SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        if (userService.findByName(chatMessage.getSender()) == null) {
            String[] bookmarks = {};
            User user = new User(chatMessage.getSender(), bookmarks);
            userService.add(user);
        }
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        template.convertAndSend("/topic/public/" + chatMessage.getSender(), chatMessage);
        messageService.add(chatMessage);
        Message welcomemes = bot.start();
        template.convertAndSend("/topic/public/" + chatMessage.getSender(), welcomemes);
        welcomemes.setUserName(chatMessage.getSender());
        messageService.add(welcomemes);
    }
}
