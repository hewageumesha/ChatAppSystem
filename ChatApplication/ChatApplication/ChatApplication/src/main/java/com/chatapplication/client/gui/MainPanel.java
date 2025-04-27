package com.chatapplication.client.gui;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JFrame {

    public MainPanel() {
        setTitle("Chat Application");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with dim background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(45, 45, 45));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Chat Application");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sloganLabel = new JLabel("Connect. Chat. Share.");
        sloganLabel.setForeground(new Color(180, 180, 180));
        sloganLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setMaximumSize(new Dimension(150, 40));

        JButton registerBtn = new JButton("Register");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setBackground(new Color(60, 179, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setMaximumSize(new Dimension(150, 40));

        // Add components with spacing
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(sloganLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(loginBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(registerBtn);

        add(mainPanel);

        // Button actions
        loginBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterForm();
        });

        setVisible(true);
    }

    // To launch this screen
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainPanel::new);
    }
}
