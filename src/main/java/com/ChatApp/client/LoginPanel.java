package com.ChatApp.client;

import java.rmi.Naming;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JFrame {
    private JPanel loginPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginPanel() {
        setTitle("🌐 Login Page");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        setContentPane(loginPanel);
        setVisible(true);

        loginButton.addActionListener(this::loginAction);
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterPanel();
        });
    }
    
    private void loginAction(ActionEvent e) {
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {
            ChatService service = (ChatService) Naming.lookup("rmi://localhost:1099/ChatService");
            boolean isAdmin = service.login(email, password);

            if (isAdmin) {
                JOptionPane.showMessageDialog(this, "Admin Login successful!");
                dispose();
                new AdminPanel();
            } else {
                String nickname = service.getNicknameByEmail(email);
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + nickname);
                dispose();

                ChatClient chatClient = new ChatClient(email, nickname);
                chatClient.startClient();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed!");
        }
    }

    public static void main(String[] args) {
        new LoginPanel();
    }
}
