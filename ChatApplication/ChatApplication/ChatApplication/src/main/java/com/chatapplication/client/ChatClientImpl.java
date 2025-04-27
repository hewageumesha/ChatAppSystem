package com.chatapplication.client;



import com.chatapplication.rmi.ChatClient;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

    private final JTextArea chatArea;
    private final int userId;
    private final int chatId;
    private final String nickName;

    public ChatClientImpl(JTextArea chatArea, int userId, int chatId, String nickName) throws RemoteException {
        this.chatArea = chatArea;
        this.userId = userId;
        this.chatId = chatId;
        this.nickName = nickName;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());  // Scroll to the bottom
        });
    }


    public boolean isSubscribed(int chatId) throws RemoteException {
        return this.chatId == chatId;
    }

    @Override
    public void notifyChatStarted(String chatTitle, int chatId) {
        System.out.println("🔔 Chat Started: " + chatTitle + " [ID: " + chatId + "]");
    }

    @Override
    public void notifyUserJoined(String nickName) {
        System.out.println("✅ " + nickName + " has joined the chat.");
    }

    @Override
    public void notifyUserLeft(String nickName) {
        System.out.println("❌ " + nickName + " has left the chat.");
    }

    @Override
    public void receiveMessage(String nickName, String message) {
        System.out.println(nickName + ": " + message);
    }

    public String getNickName() {
        return nickName;
    }
}
