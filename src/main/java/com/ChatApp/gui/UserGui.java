package com.ChatApp.gui;

import com.ChatApp.dao.UserDAO;
import com.ChatApp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.swing.*;
import java.awt.*;

public class UserGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public UserGui() {
        super("🌐 ChatApp Login/Register");

        // Layout Setup
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel title = new JLabel("Welcome to ChatApp");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        loginButton = new JButton("🔐 Login");
        registerButton = new JButton("📝 Register");

        // Event Listeners
        loginButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Login feature is under development!", "Login", JOptionPane.INFORMATION_MESSAGE);
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
                SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

                UserDAO userDAO = new UserDAO(sessionFactory);
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                userDAO.saveUser(user);

                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Grid positioning
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        add(panel);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserGui::new);
    }
}
