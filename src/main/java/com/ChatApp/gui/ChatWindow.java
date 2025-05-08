package com.ChatApp.gui;

import com.ChatApp.model.User;
import com.ChatApp.rmi.ChatClient;
import com.ChatApp.rmi.ChatService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatWindow extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    private ChatService chatService;
    private String nickname;
    private int chatId;

    public ChatWindow(User nickname) {
        this.nickname = String.valueOf(nickname);
        this.chatId = chatId;

        initUI();

        try {
            chatService = (ChatService) Naming.lookup("rmi://localhost/ChatService");
            ChatClient client = new ChatClientImpl();
            chatService.registerClient(client);
            chatService.userJoin(String.valueOf(nickname));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error connecting to chat service: " + e.getMessage());
            e.printStackTrace();
        }


    }

    private void initUI() {
        setTitle("Chat - " + nickname);
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🌟 Chat Title Bar
        JLabel titleLabel = new JLabel("Welcome, " + nickname + " 👋", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(65, 105, 225)); // Royal Blue
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // 📝 Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(Color.WHITE);
        chatArea.setForeground(Color.DARK_GRAY);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // 📨 Input Area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(240, 248, 255)); // Alice Blue

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        sendButton.addActionListener((ActionEvent e) -> {
            try {
                String message = inputField.getText().trim();
                if (!message.isEmpty()) {
                    chatService.sendMessage(nickname, message);
                    inputField.setText("");
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 🔁 RMI Client Implementation
    private class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

        protected ChatClientImpl() throws RemoteException {}

        @Override
        public void receiveMessage(String sender, String message) throws RemoteException {
            SwingUtilities.invokeLater(() -> {
                chatArea.append(sender + ": " + message + "\n");
            });
        }

        @Override
        public void receiveMessage(String message) throws RemoteException {

        }

        @Override
        public boolean isSubscribed(int chatId) throws RemoteException {
            return ChatWindow.this.chatId == chatId;
        }

        @Override
        public void notifyChatStarted(String chatTitle, int chatId) throws RemoteException {
            SwingUtilities.invokeLater(() -> chatArea.append("🔔 Chat Started: " + chatTitle + "\n"));
        }

        @Override
        public void notifyUserJoined(String nickName) throws RemoteException {
            SwingUtilities.invokeLater(() -> {
                if (chatArea != null) {
                    chatArea.append("✅ " + nickName + " joined the chat.\n");
                }
            });
        }

        @Override
        public void notifyUserLeft(String nickName) throws RemoteException {
            SwingUtilities.invokeLater(() -> chatArea.append("❌ " + nickName + " left the chat.\n"));
        }
    }
}
