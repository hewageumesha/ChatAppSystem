package com.chatapplication.client.gui;

import com.chatapplication.dao.ChatDAO;
import com.chatapplication.dao.UserDAO;
import com.chatapplication.model.Chat;
import com.chatapplication.model.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class AdminPanel extends JFrame {

    private final JComboBox<User> userComboBox = new JComboBox<>();
    private final JComboBox<Chat> chatComboBox = new JComboBox<>();

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 248, 250));

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Top Panel - Create & Load Actions
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(new TitledBorder("Admin Actions"));

        topPanel.add(createStyledButton("Create Chat", buttonFont, new Color(70, 130, 180), e -> createChat()));
        topPanel.add(createStyledButton("Load Users", buttonFont, new Color(70, 130, 180), e -> loadUsers()));
        topPanel.add(createStyledButton("Load Chats", buttonFont, new Color(70, 130, 180), e -> loadChats()));

        // Center Panel - ComboBoxes
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new TitledBorder("Select User & Chat"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("User:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(userComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Chat:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(chatComboBox, gbc);

        // Bottom Panel - Subscription and Removal
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new TitledBorder("User-Chat Management"));

        bottomPanel.add(createStyledButton("Subscribe User", buttonFont, new Color(34, 139, 34), e -> subscribeUser()));
        bottomPanel.add(createStyledButton("Unsubscribe User", buttonFont, new Color(34, 139, 34), e -> unsubscribeUser()));
        bottomPanel.add(createStyledButton("Remove User", buttonFont, new Color(34, 139, 34), e -> removeUser()));

        // Add to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // Logic Methods
    private void createChat() {
        String title = JOptionPane.showInputDialog(this, "Enter Chat Title:");
        if (title != null && !title.trim().isEmpty()) {
            Chat chat = new Chat();
            chat.setTitle(title);
            chat.setStartTime(new Date());
            new ChatDAO().save(chat);
            JOptionPane.showMessageDialog(this, "Chat created.");
        }
    }

    private void loadUsers() {
        userComboBox.removeAllItems();
        List<User> users = new UserDAO().getAllUsers();
        for (User u : users) {
            if ("USER".equalsIgnoreCase(u.getRole())) {
                userComboBox.addItem(u);
            }
        }
    }

    private void loadChats() {
        chatComboBox.removeAllItems();
        List<Chat> chats = new ChatDAO().getAllChats();
        for (Chat chat : chats) {
            chatComboBox.addItem(chat);
        }
    }

    private void subscribeUser() {
        Chat chat = (Chat) chatComboBox.getSelectedItem();
        User user = (User) userComboBox.getSelectedItem();
        if (chat != null && user != null) {
            chat.getUsers().add(user);
            new ChatDAO().save(chat);
            JOptionPane.showMessageDialog(this, "User subscribed.");
        }
    }

    private void unsubscribeUser() {
        Chat chat = (Chat) chatComboBox.getSelectedItem();
        User user = (User) userComboBox.getSelectedItem();
        if (chat != null && user != null) {
            chat.getUsers().remove(user);
            new ChatDAO().save(chat);
            JOptionPane.showMessageDialog(this, "User unsubscribed.");
        }
    }

    private void removeUser() {
        User user = (User) userComboBox.getSelectedItem();
        if (user != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove user: " + user.getUsername() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new UserDAO().deleteUser(user.getId());
                JOptionPane.showMessageDialog(this, "User removed.");
                loadUsers(); // Refresh list
            }
        }
    }

    // Button Factory
    private JButton createStyledButton(String text, Font font, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.addActionListener(action);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPanel::new);
    }
}
