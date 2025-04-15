package com.ChatApp.gui;

import com.ChatApp.dao.ChatDao;
import com.ChatApp.model.Chat;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.swing.*;
import java.awt.*;

public class AdminGui extends JFrame {
    private JButton createChatButton;
    private JButton subscribeButton;
    private JButton unsubscribeButton;

    public AdminGui() {
        super("🛠️ Admin Control Panel");

        // Use a modern layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("ChatApp Admin Panel");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Buttons
        createChatButton = new JButton("💬 Create Chat");
        subscribeButton = new JButton("➕ Subscribe User");
        unsubscribeButton = new JButton("➖ Unsubscribe User");

        // Button Actions
        createChatButton.addActionListener(e -> {
            String chatId = JOptionPane.showInputDialog(this, "Enter Chat ID:", "Create Chat", JOptionPane.QUESTION_MESSAGE);
            if (chatId != null && !chatId.trim().isEmpty()) {
                try {
                    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
                    SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                    ChatDao chatDAO = new ChatDao(sessionFactory);

                    Chat chat = new Chat();
                    chat.setChatId(chatId.trim());
                    chatDAO.saveChat(chat);

                    JOptionPane.showMessageDialog(this, "✅ Chat '" + chatId + "' created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Failed to create chat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chat ID cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        subscribeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Subscribe feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE));
        unsubscribeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Unsubscribe feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE));

        // Layout positions
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createChatButton, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(subscribeButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(unsubscribeButton, gbc);

        add(panel);
        setSize(420, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminGui::new);
    }
}
