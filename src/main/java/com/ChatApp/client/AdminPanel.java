package com.ChatApp.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.ChatApp.model.Chat;
import com.ChatApp.model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;

public class AdminPanel extends JFrame {

    private JTextArea logArea;
    private JButton btnStartChat, btnStopChat, btnRemoveUser, btnSubscribe, btnUnsubscribe;
    private JTextField txtUserId, txtChatId;
    private SessionFactory sessionFactory;
    private Chat currentChat;

    public AdminPanel(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        setTitle("🌐 Admin Dashboard - Chat Manager");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Admin Chat Management", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        getContentPane().add(header, BorderLayout.NORTH);

        // Center - Log area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        logArea.setBorder(BorderFactory.createTitledBorder("📋 Admin Log Console"));
        JScrollPane scrollPane = new JScrollPane(logArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Bottom - Buttons and input fields
        JPanel bottomPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        btnStartChat = new JButton("Start Chat");
        btnStopChat = new JButton("Stop Chat");
        btnRemoveUser = new JButton("Remove User");
        btnSubscribe = new JButton("Subscribe");
        btnUnsubscribe = new JButton("Unsubscribe");

        txtUserId = new JTextField();
        txtChatId = new JTextField();
        txtUserId.setToolTipText("User ID");
        txtChatId.setToolTipText("Chat ID");

        bottomPanel.add(btnStartChat);
        bottomPanel.add(btnStopChat);
        bottomPanel.add(new JLabel("User ID:"));
        bottomPanel.add(txtUserId);
        bottomPanel.add(new JLabel("Chat ID:"));
        bottomPanel.add(txtChatId);

        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.add(btnSubscribe);
        actionPanel.add(btnUnsubscribe);
        actionPanel.add(btnRemoveUser);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(actionPanel, BorderLayout.EAST);

        // Button actions
        btnStartChat.addActionListener(e -> startChat());
        btnStopChat.addActionListener(e -> stopChat());
        btnRemoveUser.addActionListener(e -> removeUser());
        btnSubscribe.addActionListener(e -> subscribeUser());
        btnUnsubscribe.addActionListener(e -> unsubscribeUser());

        setVisible(true);
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }

    private void startChat() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        currentChat = new Chat();
        currentChat.setStartedAt(new Timestamp(System.currentTimeMillis()));
        session.save(currentChat);
        tx.commit();
        session.close();

        log("Chat started! Chat ID: " + currentChat.getChatId());
    }

    private void stopChat() {
        if (currentChat == null) {
            log("No active chat to stop.");
            return;
        }
        log("Chat with ID " + currentChat.getChatId() + " stopped.");
        currentChat = null;
    }

    private void removeUser() {
        String userId = txtUserId.getText().trim();
        if (userId.isEmpty()) {
            log("Please enter a User ID to remove.");
            return;
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        User user = session.get(User.class, Integer.parseInt(userId));
        if (user != null) {
            session.delete(user);
            log("User ID " + userId + " has been removed from the system.");
        } else {
            log("User not found.");
        }

        tx.commit();
        session.close();
    }

    private void subscribeUser() {
        if (currentChat == null) {
            log("Start a chat before subscribing users.");
            return;
        }

        String userId = txtUserId.getText().trim();
        if (userId.isEmpty()) {
            log("Enter a User ID to subscribe.");
            return;
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        User user = session.get(User.class, Integer.parseInt(userId));
        if (user != null) {
            currentChat.getSubscribedUsers().add(user);
            session.update(currentChat);
            log("Subscribed User " + user.getNickname() + " (ID: " + userId + ") to Chat " + currentChat.getChatId());
        } else {
            log("User not found.");
        }

        tx.commit();
        session.close();
    }

    private void unsubscribeUser() {
        if (currentChat == null) {
            log("No active chat to unsubscribe from.");
            return;
        }

        String userId = txtUserId.getText().trim();
        if (userId.isEmpty()) {
            log("Enter a User ID to unsubscribe.");
            return;
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        User user = session.get(User.class, Integer.parseInt(userId));
        if (user != null && currentChat.getSubscribedUsers().contains(user)) {
            currentChat.getSubscribedUsers().remove(user);
            session.update(currentChat);
            log("Unsubscribed User " + user.getNickname() + " (ID: " + userId + ") from Chat " + currentChat.getChatId());
        } else {
            log("⚠️ User not subscribed or not found.");
        }

        tx.commit();
        session.close();
    }
}
