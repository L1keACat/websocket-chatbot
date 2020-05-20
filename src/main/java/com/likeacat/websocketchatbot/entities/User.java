package com.likeacat.websocketchatbot.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = User.COLLECTION_NAME)
public class User {

    public static final String COLLECTION_NAME = "user";

    @Id
    private String id;
    private String name;
    private String[] bookmarks;

    public User(String name, String[] bookmarks) {
        this.name = name;
        this.bookmarks = bookmarks;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBookmarks(String[] bookmarks) {
        this.bookmarks = bookmarks;
    }

    public String[] getBookmarks() {
        return bookmarks;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + "bookmarks = " + Arrays.toString(bookmarks) + '}';
    }
}
