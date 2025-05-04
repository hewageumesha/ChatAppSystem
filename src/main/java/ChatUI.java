package com.chatapplication.client;

import com.chatapplication.rmi.ChatClient;
import com.chatapplication.rmi.ChatService;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ChatUI {
    private static JTextArea chatArea;
    private static JTextField messageField;
    private static JButton sendButton;
    private static JButton leaveButton;
    private static ChatService chatService;
    private static ChatClientImpl chatClient;
    private static int userId;
    private static int chatId;
    private static String nickName;

    public static void main(String[] args) {
        // Initialize user
        userId = 1; // Replace with actual user ID logic
        chatId = 1; // Replace with actual chat ID logic
        nickName = "User1"; // Replace with actual user nick name

        // Setup GUI
        JFrame frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField();
        sendButton = new JButton("Send");
        leaveButton = new JButton("Leave");

        // Setup layout
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(leaveButton, BorderLayout.NORTH);

        // Add button actions
        sendButton.addActionListener(e -> sendMessage());
        leaveButton.addActionListener(e -> leaveChat());

        // Setup RMI
        try {
            LocateRegistry.getRegistry("localhost", 1099);
            chatService = (ChatService) Naming.lookup("rmi://localhost/ChatService");
            chatClient = new ChatClientImpl(chatArea, userId, chatId, nickName);
            chatService.registerClient(chatClient);
            chatService.userJoin(nickName);  // Notify server that user has joined

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Show the window
        frame.setVisible(true);
    }

    private static void sendMessage() {
        try {
            String message = messageField.getText();
            chatService.sendMessage(nickName, message);
            messageField.setText("");  // Clear message field
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void leaveChat() {
        try {
            chatService.userLeave(nickName);
            chatService.unregisterClient(chatClient);
            System.exit(0);  // Close the application
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

