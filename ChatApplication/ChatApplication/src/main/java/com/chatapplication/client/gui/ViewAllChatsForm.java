package com.chatapplication.client.gui;

import com.chatapplication.model.User;

import javax.swing.*;
import java.awt.*;

public class ViewAllChatsForm extends JFrame {
    private User loggedInUser;
    private JTable chatTable;
    private JButton subscribeButton;
    private JButton unsubscribeButton;
    private JLabel statusBar;

    public ViewAllChatsForm(User user) {
        this.loggedInUser = user;

        setTitle("View All Chats");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table (Dummy Data for now)
        String[] columnNames = {"Chat ID", "Chat Name", "Status"};
        Object[][] data = {
                {"1", "General Discussion", "Available"},
                {"2", "Project X Updates", "Available"},
                {"3", "Casual Chat", "Available"},
        };
        chatTable = new JTable(data, columnNames);
        chatTable.setFillsViewportHeight(true);
        chatTable.setRowHeight(25);
        chatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        chatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane tableScrollPane = new JScrollPane(chatTable);

        // Buttons
        subscribeButton = new JButton("Subscribe");
        unsubscribeButton = new JButton("Unsubscribe");

        subscribeButton.setBackground(new Color(46, 204, 113));
        subscribeButton.setForeground(Color.WHITE);
        subscribeButton.setFocusPainted(false);

        unsubscribeButton.setBackground(new Color(231, 76, 60));
        unsubscribeButton.setForeground(Color.WHITE);
        unsubscribeButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.add(subscribeButton);
        buttonPanel.add(unsubscribeButton);

        // Status Bar
        statusBar = new JLabel("Select a chat to subscribe/unsubscribe.");
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setBackground(new Color(220, 220, 220));
        statusBar.setOpaque(true);

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(250, 250, 250));
        mainPanel.add(new JLabel("  All Available Chats", SwingConstants.LEFT), BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);

        // Button Actions (Empty for now - you will connect later)
        subscribeButton.addActionListener(e -> {
            int selectedRow = chatTable.getSelectedRow();
            if (selectedRow >= 0) {
                String chatName = chatTable.getValueAt(selectedRow, 1).toString();
                statusBar.setText("Subscribed to " + chatName);
                // TODO: Call your subscribe logic here
            } else {
                JOptionPane.showMessageDialog(this, "Please select a chat to subscribe.");
            }
        });

        unsubscribeButton.addActionListener(e -> {
            int selectedRow = chatTable.getSelectedRow();
            if (selectedRow >= 0) {
                String chatName = chatTable.getValueAt(selectedRow, 1).toString();
                statusBar.setText("Unsubscribed from " + chatName);
                // TODO: Call your unsubscribe logic here
            } else {
                JOptionPane.showMessageDialog(this, "Please select a chat to unsubscribe.");
            }
        });
    }
}
