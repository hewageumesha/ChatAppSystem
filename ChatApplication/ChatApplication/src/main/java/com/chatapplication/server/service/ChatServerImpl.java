package com.chatapplication.server.service;

import com.chatapplication.model.Chat;
import com.chatapplication.rmi.ChatService;
import com.chatapplication.rmi.ChatClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServerImpl extends UnicastRemoteObject implements ChatService {

    private final List<ChatClient> clients = new CopyOnWriteArrayList<>();

    public ChatServerImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerClient(ChatClient client) throws RemoteException {
        clients.add(client);
        System.out.println("Client registered.");
    }

    @Override
    public void unregisterClient(ChatClient client) throws RemoteException {
        clients.remove(client);
        System.out.println("Client unregistered.");
    }

    @Override
    public void startChat(String chatTitle, int chatId) throws RemoteException {
        for (ChatClient client : clients) {
            client.notifyChatStarted(chatTitle, chatId);
        }
        System.out.println("Chat started: " + chatTitle);
    }

    @Override
    public void sendMessage(String nickName, String message) throws RemoteException {
        for (ChatClient client : clients) {
            client.receiveMessage(nickName,message);
        }
    }

    @Override
    public void userJoin(String nickName) throws RemoteException {
        for (ChatClient client : clients) {
            client.notifyUserJoined(nickName);
        }
    }

    @Override
    public void userLeave(String nickName) throws RemoteException {
        for (ChatClient client : clients) {
            client.notifyUserLeft(nickName);
        }
    }

    @Override
    public List<Chat> getAllChats() {
        return null;
    }
}
