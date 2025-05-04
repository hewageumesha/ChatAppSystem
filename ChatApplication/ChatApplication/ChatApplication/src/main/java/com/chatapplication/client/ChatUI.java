package com.chatapplication.client;

import com.chatapplication.rmi.ChatClient;
import com.chatapplication.rmi.ChatService;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Main Chat UI for Chat Application
 */
public class ChatUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton leaveButton;

    private ChatService chatService;
    private ChatClientImpl chatClient;

    private int userId;
    private int chatId;
    private String nickName;

    public ChatUI(int userId, int chatId, String nickName) {
        this.userId = userId;
        this.chatId = chatId;
        this.nickName = nickName;

        setTitle("💬 Chat - " + nickName);
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 📝 Chat display area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // 🔥 Message input and Send button
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        messageField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // 🚪 Leave button
        leaveButton = new JButton("Leave Chat");
        leaveButton.setBackground(Color.RED);
        leaveButton.setForeground(Color.WHITE);
        leaveButton.setFocusPainted(false);
        leaveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        leaveButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("👋 Welcome, " + nickName, SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        topPanel.add(leaveButton, BorderLayout.EAST);
        topPanel.setBackground(new Color(240, 248, 255));

        add(topPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.SOUTH);

        // 🖥️ RMI Setup
        try {
            LocateRegistry.getRegistry("localhost", 1099); // optional if already started externally
            chatService = (ChatService) Naming.lookup("rmi://localhost/ChatService");

            chatClient = new ChatClientImpl(chatArea, userId, chatId, nickName);
            chatService.registerClient(chatClient);
            chatService.userJoin(nickName);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Failed to connect to server: " + ex.getMessage());
            ex.printStackTrace();
        }

        // 🎯 Event Listeners
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        leaveButton.addActionListener(e -> leaveChat());

        setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                chatService.sendMessage(nickName, message);
                messageField.setText("");
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "❌ Failed to send message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void leaveChat() {
        try {
            chatService.userLeave(nickName);
            chatService.unregisterClient(chatClient);
            JOptionPane.showMessageDialog(this, "👋 You have left the chat.");
            System.exit(0);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to leave chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Hardcoded test user; normally you'd pass userId, chatId, nickname from login
        SwingUtilities.invokeLater(() -> new ChatUI(1, 1, "User1"));
    }
}
