package gui;

import models.User;
import network.ChatServerImpl;
import services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginScreen extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JCheckBox showPasswordCheckbox;

    private static boolean isServerStarted = false;

    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color TEXT_COLOR = Color.WHITE;

    private void startChatServer() {
        if (!isServerStarted) {
            try {
                network.ChatService chatService = new ChatServerImpl();
                Registry registry = LocateRegistry.createRegistry(1099);
                registry.rebind("ChatService", chatService);
                System.out.println("Chat server is running...");
                isServerStarted = true;
            } catch (Exception e) {
                System.err.println("Failed to start chat server: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public LoginScreen() {
        startChatServer();

        setTitle("ChatNest - Login");
        setSize(400, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Login for ChatApp");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        styleTextField(emailField);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Show password checkbox
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setBackground(BACKGROUND_COLOR);
        showPasswordCheckbox.setForeground(TEXT_COLOR);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(showPasswordCheckbox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        registerButton = new JButton("Register");
        styleButton(registerButton);
        buttonPanel.add(registerButton);

        loginButton = new JButton("Login");
        styleButton(loginButton);
        buttonPanel.add(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        // UserService
        UserService userService = new UserService();

        // Show password toggle
        showPasswordCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckbox.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('\u2022');
                }
            }
        });

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                // Reset borders
                resetFieldBorders();

                boolean isValid = true;

                // Empty checks
                if (email.isEmpty()) {
                    emailField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    isValid = false;
                }

                if (password.isEmpty()) {
                    passwordField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    isValid = false;
                }

                // Email format
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    emailField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    JOptionPane.showMessageDialog(LoginScreen.this, "Please enter a valid email address.");
                    return;
                }

                if (!isValid) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Please fill in all fields.");
                    return;
                }

                // Proceed
                User user = userService.loginUser(email, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Login successful!");
                    emailField.setText("");
                    passwordField.setText("");

                    if (email.equalsIgnoreCase("admin@gmail.com")) {
                        new AdminPanel().setVisible(true);
                    } else {
                        new ChatWindow(user).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Invalid email or password.");
                }
            }
        });

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationScreen().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }

    private void styleButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        field.setCaretColor(TEXT_COLOR);
    }

    private void resetFieldBorders() {
        emailField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }
}
