package com.ChatApp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String chatId;
    private List<User> subscribedUsers;
    private List<String> chatLogs;

    public Chat() {
        this.subscribedUsers = new ArrayList<>();
        this.chatLogs = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }
    public List<User> getSubscribedUsers() { return subscribedUsers; }
    public void setSubscribedUsers(List<User> subscribedUsers) { this.subscribedUsers = subscribedUsers; }
    public List<String> getChatLogs() { return chatLogs; }
    public void setChatLogs(List<String> chatLogs) { this.chatLogs = chatLogs; }

    public void startChat() {
        System.out.println("Chat started at: " + new java.util.Date());
    }

    public void joinChat(User user) {
        subscribedUsers.add(user);
        System.out.println(user.getNickname() + " has joined: " + new java.util.Date());
    }

    public void leaveChat(User user) {
        subscribedUsers.remove(user);
        System.out.println(user.getNickname() + " left: " + new java.util.Date());
    }

    public void saveChatLogs() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter("chat_" + chatId + ".txt")) {
            for (String log : chatLogs) {
                writer.println(log);
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error saving chat logs: " + e.getMessage());
        }
    }
}

