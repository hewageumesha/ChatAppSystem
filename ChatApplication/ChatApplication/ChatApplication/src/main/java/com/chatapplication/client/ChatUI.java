package com.chatapplication.client;

import com.chatapplication.rmi.ChatClient;
import com.chatapplication.rmi.ChatService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        // Initialize user details (replace these with actual login logic if needed)
        userId = 1;
        chatId = 1;
        nickName = "User1";

        // Setup main frame
        JFrame frame = new JFrame("Chat Application - " + nickName);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null); // Center window

        // Chat area (message display)
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Message input and send button
        messageField = new JTextField();
        sendButton = new JButton("Send");
        leaveButton = new JButton("Leave Chat");

        // Layout panels
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(leaveButton);

        frame.setLayout(new BorderLayout(5, 5));
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage()); // Press Enter to send
        leaveButton.addActionListener(e -> leaveChat());

        // Handle window close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                leaveChat();
            }
        });

        // Setup RMI
        try {
            LocateRegistry.getRegistry("localhost", 1099); // Ensure registry is running
            chatService = (ChatService) Naming.lookup("rmi://localhost/ChatService");

            chatClient = new ChatClientImpl(chatArea, userId, chatId, nickName);
            chatService.registerClient(chatClient);
            chatService.userJoin(nickName);  // Notify server of user join

            appendMessage("Connected to chat as " + nickName + ".");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to connect to chat server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Display window
        frame.setVisible(true);
    }

    private static void sendMessage() {
        try {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                chatService.sendMessage(nickName, message);
                messageField.setText("");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            appendMessage("Failed to send message: " + e.getMessage());
        }
    }

    private static void leaveChat() {
        try {
            if (chatService != null && chatClient != null) {
                chatService.userLeave(nickName);
                chatService.unregisterClient(chatClient);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private static void appendMessage(String message) {
        chatArea.append(message + "\n");
    }
}
