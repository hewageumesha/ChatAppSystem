package com.chatapplication.client.gui;

import com.chatapplication.model.User;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class UserPanel extends JFrame {
    private User loggedInUser;
    private JLabel welcomeLabel;
    private JButton updateProfileButton;
    private JButton viewChatsButton;
    private JButton joinChatButton;

    public UserPanel(User user) {
        this.loggedInUser = user;
        setTitle("User Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        welcomeLabel = new JLabel("Welcome, " + user.getNickname());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        updateProfileButton = new JButton("Update Profile");
        viewChatsButton = new JButton("View Chats");
        joinChatButton = new JButton("Join Chat");

        for (JButton btn : new JButton[]{
                updateProfileButton,
                viewChatsButton,
                joinChatButton
        }) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBackground(new Color(100, 149, 237));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(300, 40));
            btn.setMargin(new Insets(10, 20, 10, 20));
            panel.add(Box.createVerticalStrut(15));
            panel.add(btn);
        }

        updateProfileButton.addActionListener(e -> {
            new UpdateProfileForm(loggedInUser);
        });

        viewChatsButton.addActionListener(e -> {
            try {
                new ViewAllChatsForm(loggedInUser);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        joinChatButton.addActionListener(e -> {
            new ChatWindow(loggedInUser);
        });

        panel.add(Box.createVerticalStrut(30));
        panel.add(welcomeLabel);

        add(panel);
        setVisible(true);
    }
}
