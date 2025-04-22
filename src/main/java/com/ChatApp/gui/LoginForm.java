package com.ChatApp.gui;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private AuthService authService = new AuthService();

    public LoginForm() {
        setTitle("Login");
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        add(loginButton);

        JButton goToRegister = new JButton("Register");
        goToRegister.addActionListener(e -> {
            new RegisterForm();
            dispose();
        });
        add(goToRegister);

        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        if (authService.login(user, pass)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            // Proceed to profile or main chat screen
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
        }
    }
}

