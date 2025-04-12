package com.ChatApp.gui;

import com.ChatApp.dao.UserDao;
import com.ChatApp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public UserGui() {
        super("User Login/Register");
        setLayout(new FlowLayout());

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login logic
                System.out.println("Login clicked");
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle register logic
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
                SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().getSessionFactoryBuilder().build();
                UserDao userDAO = new UserDao(sessionFactory);

                User user = new User();
                user.setUsername(usernameField.getText());
                user.setPassword(new String(passwordField.getPassword()));
                userDAO.saveUser(user);
            }
        });

        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserGui();
            }
        });
    }
}

