package gui;

import models.Chat;
import models.User;
import services.ChatService;
import services.SubscriptionService;
import services.UserService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminPanel extends JFrame {

    private JButton createChatButton;
    private JButton viewUsersButton;
    private JButton viewChatsButton;
    private JButton subscribeUserButton;
    private JButton unsubscribeUserButton;
    private JButton removeUserButton;
    private JButton editUserButton;
    private JButton backButton;
    private JComboBox<User> userComboBox;
    private JComboBox<Chat> chatComboBox;

    // Modern theme colors - matching the sample
    private static final Color THEME_PRIMARY = new Color(70, 130, 180);  // Steel blue
    private static final Color THEME_ACTION = new Color(34, 139, 34);    // Forest green
    private static final Color THEME_BACKGROUND = new Color(245, 248, 250); // Light gray-blue
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout and padding
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(THEME_BACKGROUND);

        // Top panel - Admin Actions
        JPanel topPanel = createTopPanel();

        // Center panel - Selection Panel
        JPanel centerPanel = createCenterPanel();

        // Bottom panel - User-Chat Management
        JPanel bottomPanel = createBottomPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Initialize services
        setupActionListeners();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(new TitledBorder("Admin Actions"));

        createChatButton = createStyledButton("Create Chat", BUTTON_FONT, THEME_PRIMARY);
        viewUsersButton = createStyledButton("Load Users", BUTTON_FONT, THEME_PRIMARY);
        viewChatsButton = createStyledButton("Load Chats", BUTTON_FONT, THEME_PRIMARY);

        topPanel.add(createChatButton);
        topPanel.add(viewUsersButton);
        topPanel.add(viewChatsButton);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new TitledBorder("Select User & Chat"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);

        // Initialize the combo boxes
        userComboBox = new JComboBox<>();
        chatComboBox = new JComboBox<>();

        // Set preferred size for combo boxes
        Dimension comboSize = new Dimension(300, 30);
        userComboBox.setPreferredSize(comboSize);
        chatComboBox.setPreferredSize(comboSize);

        // Add to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("User:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(userComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("Chat:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(chatComboBox, gbc);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new TitledBorder("User-Chat Management"));

        subscribeUserButton = createStyledButton("Subscribe User", BUTTON_FONT, THEME_ACTION);
        unsubscribeUserButton = createStyledButton("Unsubscribe User", BUTTON_FONT, THEME_ACTION);
        removeUserButton = createStyledButton("Remove User", BUTTON_FONT, THEME_ACTION);
        editUserButton = createStyledButton("Edit User Profile", BUTTON_FONT, THEME_ACTION);
        backButton = createStyledButton("Back to Login", BUTTON_FONT, THEME_PRIMARY);

        bottomPanel.add(subscribeUserButton);
        bottomPanel.add(unsubscribeUserButton);
        bottomPanel.add(removeUserButton);
        bottomPanel.add(editUserButton);

        // Add back button in a separate panel at the bottom
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setOpaque(false);
        backPanel.add(backButton);

        JPanel combinedBottomPanel = new JPanel(new BorderLayout());
        combinedBottomPanel.setOpaque(false);
        combinedBottomPanel.add(bottomPanel, BorderLayout.CENTER);
        combinedBottomPanel.add(backPanel, BorderLayout.SOUTH);

        return combinedBottomPanel;
    }

    private void setupActionListeners() {
        ChatService chatService = new ChatService();
        UserService userService = new UserService();
        SubscriptionService subscriptionService = new SubscriptionService();

        createChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show input dialog to get the chat title
                String title = JOptionPane.showInputDialog(AdminPanel.this, "Enter chat title:", "Create New Chat", JOptionPane.PLAIN_MESSAGE);

                if (title != null && !title.trim().isEmpty()) {
                    Chat chat = chatService.startChat(title.trim());  // pass the title to startChat method
                    if (chat != null) {
                        JOptionPane.showMessageDialog(AdminPanel.this, "Chat '" + title + "' created successfully!");
                        refreshChatComboBox(chatService);
                    } else {
                        JOptionPane.showMessageDialog(AdminPanel.this, "Failed to create chat.");
                    }
                } else if (title != null) {
                    // Title was empty string
                    JOptionPane.showMessageDialog(AdminPanel.this, "Chat title cannot be empty.");
                }
                // if title == null, user cancelled input dialog; do nothing
            }
        });

        viewUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshUserComboBox(userService);

                // Also show the user list in a dialog
                StringBuilder userList = new StringBuilder("<html><div style='font-family:Arial; font-size:12px; padding:10px;'>");
                userList.append("<h2 style='color:#075E54;'>Registered Users</h2>");
                userList.append("<table style='width:100%; border-collapse:collapse;'>");
                userList.append("<tr style='background-color:#075E54; color:white;'><th style='padding:8px;'>ID</th><th style='padding:8px;'>Nickname</th></tr>");

                boolean alternate = false;
                for (User user : userService.getAllUsers()) {
                    String rowStyle = alternate ? "background-color:#ECE5DD;" : "background-color:#FFFFFF;";
                    userList.append("<tr style='").append(rowStyle).append("'>");
                    userList.append("<td style='padding:8px; text-align:center;'>").append(user.getId()).append("</td>");
                    userList.append("<td style='padding:8px;'>").append(user.getNickname()).append("</td>");
                    userList.append("</tr>");
                    alternate = !alternate;
                }

                userList.append("</table></div></html>");
                JOptionPane.showMessageDialog(AdminPanel.this, userList.toString(), "ChatNest Users", JOptionPane.PLAIN_MESSAGE);
            }
        });

        viewChatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshChatComboBox(chatService);

                // Also show the chat list in a dialog
                StringBuilder chatList = new StringBuilder("<html><div style='font-family:Arial; font-size:12px; padding:10px;'>");
                chatList.append("<h2 style='color:#075E54;'>Available Chats</h2>");
                chatList.append("<table style='width:100%; border-collapse:collapse;'>");
                chatList.append("<tr style='background-color:#075E54; color:white;'>");
                chatList.append("<th style='padding:8px;'>Chat ID</th>");
                chatList.append("<th style='padding:8px;'>Created</th>");
                chatList.append("<th style='padding:8px;'>Status</th>");
                chatList.append("</tr>");

                boolean alternate = false;
                for (Chat chat : chatService.getAllChats()) {
                    String rowStyle = alternate ? "background-color:#ECE5DD;" : "background-color:#FFFFFF;";
                    chatList.append("<tr style='").append(rowStyle).append("'>");
                    chatList.append("<td style='padding:8px; text-align:center;'>").append(chat.getId()).append("</td>");
                    chatList.append("<td style='padding:8px;'>").append(chat.getStartTime()).append("</td>");

                    // Determine chat status
                    String status = chat.getEndTime() == null ? "Active" : "Ended";
                    String statusColor = chat.getEndTime() == null ? "green" : "red";
                    chatList.append("<td style='padding:8px; color:").append(statusColor).append(";'>").append(status).append("</td>");

                    chatList.append("</tr>");
                    alternate = !alternate;
                }

                chatList.append("</table></div></html>");
                JOptionPane.showMessageDialog(AdminPanel.this, chatList.toString(), "ChatNest Chats", JOptionPane.PLAIN_MESSAGE);
            }
        });

        subscribeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User selectedUser = (User) userComboBox.getSelectedItem();
                Chat selectedChat = (Chat) chatComboBox.getSelectedItem();

                if (selectedUser == null || selectedChat == null) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please select both a user and a chat.");
                    return;
                }

                subscriptionService.subscribeUserToChat(selectedUser, selectedChat);
                JOptionPane.showMessageDialog(AdminPanel.this, "User subscribed successfully!");
            }
        });

        unsubscribeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User selectedUser = (User) userComboBox.getSelectedItem();
                Chat selectedChat = (Chat) chatComboBox.getSelectedItem();

                if (selectedUser == null || selectedChat == null) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please select both a user and a chat.");
                    return;
                }

                subscriptionService.unsubscribeUserFromChat(selectedUser, selectedChat);
                JOptionPane.showMessageDialog(AdminPanel.this, "User unsubscribed successfully!");
            }
        });

        removeUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User selectedUser = (User) userComboBox.getSelectedItem();
                if (selectedUser != null) {
                    int confirm = JOptionPane.showConfirmDialog(AdminPanel.this,
                            "Are you sure you want to delete user: " + selectedUser.getNickname() + "?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // First delete subscriptions
                        subscriptionService.deleteSubscriptionsByUserId(selectedUser.getId());

                        // Then delete the user
                        boolean deleted = userService.deleteUser(selectedUser.getId());
                        if (deleted) {
                            JOptionPane.showMessageDialog(AdminPanel.this, "User deleted successfully.");
                            refreshUserComboBox(userService);
                        } else {
                            JOptionPane.showMessageDialog(AdminPanel.this, "Failed to delete user.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please select a user to delete.");
                }
            }
        });

        editUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User selectedUser = (User) userComboBox.getSelectedItem();

                if (selectedUser == null) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Please select a user to edit.");
                    return;
                }

                dispose(); // Close admin panel
                // Open the admin profile update screen for the selected user
                new AdminProfileUpdateScreen(selectedUser).setVisible(true);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close admin panel
                new LoginScreen().setVisible(true); // Open login screen
            }
        });
    }

    private void refreshUserComboBox(UserService userService) {
        userComboBox.removeAllItems();
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            userComboBox.addItem(user);
        }
    }

    private void refreshChatComboBox(ChatService chatService) {
        chatComboBox.removeAllItems();
        List<Chat> chats = chatService.getAllChats();
        for (Chat chat : chats) {
            chatComboBox.addItem(chat);
        }
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
    }
}