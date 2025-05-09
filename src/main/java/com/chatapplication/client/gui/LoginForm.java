package com.chatapplication.client.gui;

import com.chatapplication.dao.UserDAO;
import com.chatapplication.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    JTextField emailField = new JTextField(20);
    JPasswordField passwordField = new JPasswordField(20);

    public LoginForm() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(45, 45, 45)); // Dark background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(loginBtn, gbc);

        add(mainPanel);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User user = new UserDAO().getByEmailAndPassword(email, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + user.getNickname());
                dispose(); // close login window

                if (email.equalsIgnoreCase("admin@gmail.com")) {
                    new AdminPanel(); // Admin GUI
                } else {
                    //new ChatWindow(user.getNickname(),1); // User GUI with user passed
                    new UserPanel(user);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login failed!");
            }
        });

        setVisible(true);
    }
}
