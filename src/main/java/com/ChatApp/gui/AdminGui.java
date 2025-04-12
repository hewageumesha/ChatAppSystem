package com.ChatApp.gui;

import com.ChatApp.dao.ChatDao;
import com.ChatApp.model.Chat;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminGui extends JFrame {
    private JButton createChatButton;
    private JButton subscribeButton;
    private JButton unsubscribeButton;

    public AdminGui() {
        super("Admin Panel");
        setLayout(new FlowLayout());

        createChatButton = new JButton("Create Chat");
        subscribeButton = new JButton("Subscribe User");
        unsubscribeButton = new JButton("Unsubscribe User");

        createChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle create chat logic
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
                SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().getSessionFactoryBuilder().build();
                ChatDao chatDAO = new ChatDao(sessionFactory);

                Chat chat = new Chat();
                chat.setChatId("Chat1");
                chatDAO.saveChat(chat);
            }
        });

        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle subscribe logic
                System.out.println("Subscribe clicked");
            }
        });

        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle unsubscribe logic
                System.out.println("Unsubscribe clicked");
            }
        });

        add(createChatButton);
        add(subscribeButton);
        add(unsubscribeButton);

        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminGui();
            }
        });
    }
}

