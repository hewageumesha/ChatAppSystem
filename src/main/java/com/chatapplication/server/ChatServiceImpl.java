package com.chatapplication.server;


import com.chatapplication.model.Chat;
import com.chatapplication.model.User;
import com.chatapplication.rmi.ChatClient;
import com.chatapplication.rmi.ChatService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private final Set<ChatClient> clients = new HashSet<>();
    private boolean chatActive = false;
    private StringBuilder chatLog = new StringBuilder();
    private int activeChatId = -1;

    public ChatServiceImpl() throws RemoteException {}

    public synchronized void registerClient(ChatClient client) throws RemoteException {
        clients.add(client);
    }

    public synchronized void unregisterClient(ChatClient client) throws RemoteException {
        clients.remove(client);
    }

    public synchronized void startChat(String chatTitle, int chatId) throws RemoteException {
        if (chatActive) return;
        activeChatId = chatId;
        chatActive = true;
        String startMsg = "Chat started at: " + new Date();
        chatLog.append(startMsg).append("\n");
        for (ChatClient client : clients) {
            if (client.isSubscribed(chatId))
                client.receiveMessage("[System] " + startMsg);
        }
    }

    public synchronized void sendMessage(String nickName, String message) throws RemoteException {
        String msg = nickName + ": " + message;
        chatLog.append(msg).append("\n");

        for (ChatClient client : clients) {
            if (client.isSubscribed(activeChatId))
                client.receiveMessage(msg);
        }

        if (message.equalsIgnoreCase("Bye")) {
            userLeave(nickName);
        }
    }

    public synchronized void userJoin(String nickName) throws RemoteException {
        String joinMsg = "\"" + nickName + "\" has joined : " + new Date();
        chatLog.append(joinMsg).append("\n");

        for (ChatClient client : clients) {
            if (client.isSubscribed(activeChatId))
                client.receiveMessage(joinMsg);
        }
    }

    public synchronized void userLeave(String nickName) throws RemoteException {
        String leaveMsg = "\"" + nickName + "\" left : " + new Date();
        chatLog.append(leaveMsg).append("\n");

        clients.removeIf(client -> {
            try {
                return !client.isSubscribed(activeChatId);
            } catch (RemoteException e) {
                return true;
            }
        });

        if (clients.stream().noneMatch(client -> {
            try {
                return client.isSubscribed(activeChatId);
            } catch (RemoteException e) {
                return false;
            }
        })) {
            try {
                stopChat();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public List<Chat> getAllChats() {
        return null;
    }

    private void stopChat() throws IOException {
        String stopMsg = "Chat stopped at: " + new Date();
        chatLog.append(stopMsg).append("\n");

        for (ChatClient client : clients) {
            client.receiveMessage("[System] " + stopMsg);
        }

        // Save to txt
        String filePath = "chat_" + activeChatId + "_" + System.currentTimeMillis() + ".txt";
        Files.write(Paths.get(filePath), chatLog.toString().getBytes());

        // Update DB with Hibernate
        new ChatDAO().notify();

        chatLog = new StringBuilder();
        chatActive = false;
        activeChatId = -1;
    }

    private class ChatDAO {
    }
}

