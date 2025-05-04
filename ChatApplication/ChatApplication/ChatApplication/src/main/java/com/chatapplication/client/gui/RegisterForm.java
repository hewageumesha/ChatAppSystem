package com.chatapplication.client.gui;

import com.chatapplication.dao.UserDAO;
import com.chatapplication.model.User;
import com.chatapplication.model.enums.Role;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class RegisterForm extends JFrame {
    private JTextField emailField = new JTextField(20);
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JTextField nicknameField = new JTextField(20);
    private JButton choosePicButton = new JButton("Choose Profile Pic");
    private JButton registerButton = new JButton("Register");
    private byte[] profilePic = null;

    public RegisterForm() {
        setTitle("User Registration");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 💙 Header
        JLabel headerLabel = new JLabel("Create Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(65, 105, 225)); // Royal Blue
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // 📋 Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255)); // Alice Blue
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Helper to add rows
        int y = 0;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridy = ++y; gbc.gridx = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridy = ++y; gbc.gridx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridy = ++y; gbc.gridx = 0;
        formPanel.add(new JLabel("Nickname:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nicknameField, gbc);

        gbc.gridy = ++y; gbc.gridx = 0;
        formPanel.add(new JLabel("Profile Picture:"), gbc);
        gbc.gridx = 1;
        formPanel.add(choosePicButton, gbc);

        // 🎨 Style button
        choosePicButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        choosePicButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        choosePicButton.setForeground(Color.WHITE);
        choosePicButton.setFocusPainted(false);

        choosePicButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    profilePic = Files.readAllBytes(file.toPath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // ✅ Register Button
        gbc.gridy = ++y; gbc.gridx = 0; gbc.gridwidth = 2;
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        formPanel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            User user = new User();
            user.setEmail(emailField.getText());
            user.setUsername(usernameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            user.setNickname(nicknameField.getText());
            user.setProfilePic(profilePic);
            user.setRole(Role.USER); // ✅


            new UserDAO().save(user);
            JOptionPane.showMessageDialog(this, "🎉 Registered successfully!");

            new LoginForm();
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            dispose(); // close registration window

        });

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
