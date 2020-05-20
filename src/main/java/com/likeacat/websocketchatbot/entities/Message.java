package com.likeacat.websocketchatbot.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Message.COLLECTION_NAME)
public class Message {
    public static final String COLLECTION_NAME = "messages";

    @Id
    private String id;
    private MessageType type;
    private String content;
    private String sender;
    private String userName;

    public enum MessageType{
        CHAT,
        JOIN,
        LEAVE
    }

    public String getId() {
        return id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
