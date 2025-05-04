package com.chatapplication.client.gui;

import com.chatapplication.dao.UserDAO;
import com.chatapplication.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class UpdateProfileForm extends JFrame {
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JTextField nicknameField = new JTextField(20);
    private JButton choosePicButton = new JButton("Change Profile Picture");
    private JButton updateButton = new JButton("Update");
    private byte[] profilePic = null;

    public UpdateProfileForm(User loggedInUser) {
        setTitle("Update Profile");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🧑 Header
        JLabel headerLabel = new JLabel("Update Your Profile", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(70, 130, 180));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // 📋 Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pre-fill fields
        usernameField.setText(loggedInUser.getUsername());
        passwordField.setText(loggedInUser.getPassword());
        nicknameField.setText(loggedInUser.getNickname());

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y;
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

        choosePicButton.setBackground(new Color(100, 149, 237));
        choosePicButton.setForeground(Color.WHITE);
        choosePicButton.setFocusPainted(false);
        choosePicButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    profilePic = Files.readAllBytes(file.toPath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load picture!");
                    ex.printStackTrace();
                }
            }
        });

        // 🔄 Update Button
        gbc.gridy = ++y; gbc.gridx = 0; gbc.gridwidth = 2;
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setBackground(new Color(60, 179, 113));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        formPanel.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            loggedInUser.setUsername(usernameField.getText());
            loggedInUser.setPassword(new String(passwordField.getPassword()));
            loggedInUser.setNickname(nicknameField.getText());
            if (profilePic != null) {
                loggedInUser.setProfilePic(profilePic);
            }

            boolean success = new UserDAO().update(loggedInUser);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Profile updated successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update profile.");
            }
        });

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
