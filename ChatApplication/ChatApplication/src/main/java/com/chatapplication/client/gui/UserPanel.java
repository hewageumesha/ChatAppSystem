package com.chatapplication.client.gui;

import com.chatapplication.model.User;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JFrame {
    private User loggedInUser;
    private JLabel welcomeLabel;
    private JButton updateProfileButton;
    private JButton subscribeChatButton;
    private JButton unsubscribeChatButton;
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
        subscribeChatButton = new JButton("Subscribe to Chat");
        unsubscribeChatButton = new JButton("Unsubscribe from Chat");
        joinChatButton = new JButton("Join Chat");

        for (JButton btn : new JButton[]{
                updateProfileButton,
                subscribeChatButton,
                unsubscribeChatButton,
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

//        subscribeChatButton.addActionListener(e -> {
//            new SubscribeChatForm(loggedInUser);
//        });
//
//        unsubscribeChatButton.addActionListener(e -> {
//            new UnsubscribeChatForm(loggedInUser);
//        });
//
//        joinChatButton.addActionListener(e -> {
//            new ChatWindow(loggedInUser);
//        }
  //      );

        panel.add(Box.createVerticalStrut(30));
        panel.add(welcomeLabel);

        add(panel);
        setVisible(true);
    }
}
