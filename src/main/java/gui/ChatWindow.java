package gui;

import models.User;
import network.ChatService;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatWindow extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton leaveChatButton;
    private JButton subscribeButton;
    private JButton unsubscribeButton;
    private JButton updateProfileButton;
    private JButton backButton;
    private JComboBox<models.Chat> chatComboBox;
    private List<String> connectedUsers;

    private User user;
    private boolean isChatActive = true;
    private ChatService chatService;
    private services.SubscriptionService subscriptionService;
    private ChatWindowObserver observer;
    private network.ChatClientCallback callback;
    private String callbackId;
    private services.UserService userService;
    private models.Chat currentChat;

    public ChatWindow(User user) {
        this.user = user;
        this.connectedUsers = new ArrayList<>();
        this.subscriptionService = new services.SubscriptionService();
        this.userService = new services.UserService();

        setTitle(user.getNickname());
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();

        // Create and register the observer
        this.observer = new ChatWindowObserver(this);
        subscriptionService.addObserver(this.observer);

        // Connect to the RMI chat service
        connectToChatService();

        // Add window closing handler
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                leaveChat();
            }
        });

        // Display welcome messages
        displayMessage("🔔 Chat started at: " + formatDateTime(LocalDateTime.now()));
        displayMessage("✅ " + user.getNickname() + " has joined the chat");
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 🌟 Chat Title Bar
        // Container Panel for Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(65, 105, 225)); // Royal Blue
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

// Profile Picture Label
        JLabel userPicLabel = new JLabel();
        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(user.getProfilePicture());
                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                userPicLabel.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                userPicLabel.setText("👤");
                userPicLabel.setFont(new Font("Arial", Font.PLAIN, 40));
                userPicLabel.setForeground(Color.WHITE);
            }
        } else {
            userPicLabel.setText("👤");
            userPicLabel.setFont(new Font("Arial", Font.PLAIN, 40));
            userPicLabel.setForeground(Color.WHITE);
        }

// Welcome Text Label
        JLabel titleLabel = new JLabel("Welcome, " + user.getNickname());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

// Combine Picture and Text Horizontally
        JPanel combinedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        combinedPanel.setBackground(new Color(65, 105, 225)); // Match background
        combinedPanel.add(userPicLabel);
        combinedPanel.add(titleLabel);

        titlePanel.add(combinedPanel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);


        // 📝 Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(Color.WHITE);
        chatArea.setForeground(Color.DARK_GRAY);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // 🛠️ Control Panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(240, 248, 255)); // Alice Blue
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Chat selection controls
        JPanel chatControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        chatControlPanel.setBackground(new Color(240, 248, 255));

        chatComboBox = new JComboBox<>();
        chatComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        populateChatsDropdown();

        subscribeButton = createStyledButton("Subscribe", new Color(60, 179, 113));
        unsubscribeButton = createStyledButton("Unsubscribe", new Color(220, 20, 60));

        chatControlPanel.add(new JLabel("Available Chats:"));
        chatControlPanel.add(chatComboBox);
        chatControlPanel.add(subscribeButton);
        chatControlPanel.add(unsubscribeButton);

        controlPanel.add(chatControlPanel, BorderLayout.NORTH);

        // 📨 Input Area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(240, 248, 255));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        sendButton = createStyledButton("Send", new Color(65, 105, 225));
        sendButton.setPreferredSize(new Dimension(100, 40));

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        actionPanel.setBackground(new Color(240, 248, 255));

        updateProfileButton = createStyledButton("Profile", new Color(255, 140, 0));
        leaveChatButton = createStyledButton("Leave", new Color(220, 20, 60));
        backButton = createStyledButton("Back to Login", new Color(100, 149, 237));

        actionPanel.add(updateProfileButton);
        actionPanel.add(leaveChatButton);
        actionPanel.add(backButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Add action listeners
        setupActionListeners();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void connectToChatService() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            chatService = (ChatService) registry.lookup("ChatService");

            callback = new network.ChatClientCallbackImpl(this);
            callbackId = chatService.registerCallbackWithId(user.getNickname(), callback);
            chatService.notifyUserJoined(user.getNickname());

            connectedUsers.add(user.getNickname());
            displayMessage("✅ Connected to chat server successfully");
        } catch (Exception e) {
            String errorMsg = "Failed to connect to chat server: " + e.getMessage() +
                    "\n\nYou can still use the chat window in local mode.";
            JOptionPane.showMessageDialog(this, errorMsg, "Connection Error", JOptionPane.WARNING_MESSAGE);
            connectedUsers.add(user.getNickname());
            displayMessage("⚠️ WARNING: Not connected to chat server. Messages will only be displayed locally.");
        }
    }

    private void setupActionListeners() {
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        leaveChatButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to leave the chat?",
                    "Confirm Leave",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                leaveChat();
            }
        });

        subscribeButton.addActionListener(e -> subscribeToChat());
        unsubscribeButton.addActionListener(e -> unsubscribeFromChat());

        updateProfileButton.addActionListener(e -> {
            dispose();
            new ProfileUpdateScreen(user).setVisible(true);
        });

        backButton.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });

        chatComboBox.addActionListener(e -> updateCurrentChatFromComboBox());
    }

    public void displayMessage(String message) {
        if (message.contains("Chat started at:") ||
                message.contains("has joined the chat") ||
                message.contains("has left the chat") ||
                message.contains("Connected to chat server") ||
                message.contains("WARNING:")) {

            // System message
            chatArea.append(message + "\n");
        } else if (message.contains(":")) {
            // User message
            String[] parts = message.split(":", 2);
            String sender = parts[0].trim();
            String content = parts[1].trim();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

            if (sender.equals(user.getNickname())) {
                // Current user's message (right-aligned)
                chatArea.append(String.format("[%s] You: %s\n", timestamp, content));
            } else {
                // Other user's message (left-aligned)
                chatArea.append(String.format("%s [%s]: %s\n", sender, timestamp, content));
            }
        } else {
            // Regular message
            chatArea.append(message + "\n");
        }

        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void sendMessage() {
        if (!isChatActive) {
            JOptionPane.showMessageDialog(this, "You have already left the chat.");
            return;
        }

        String message = inputField.getText().trim();
        if (message.isEmpty()) {
            return;
        }

        if (message.equalsIgnoreCase("Bye")) {
            leaveChat();
            return;
        }

        if (currentChat == null) {
            JOptionPane.showMessageDialog(this,
                    "Please subscribe to a chat first before sending messages.",
                    "No Chat Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!subscriptionService.isUserSubscribedToChat(user, currentChat)) {
            JOptionPane.showMessageDialog(this,
                    "You are not subscribed to this chat. Please subscribe first.",
                    "Not Subscribed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (chatService == null) {
                displayMessage("You: " + message);
                JOptionPane.showMessageDialog(this,
                        "Message displayed locally only. Not connected to chat server.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                chatService.broadcastMessage(user.getNickname() + ": " + message);
            }

            inputField.setText("");
        } catch (Exception e) {
            displayMessage("You: " + message);
            JOptionPane.showMessageDialog(this,
                    "Failed to send message to server: " + e.getMessage() + "\nMessage displayed locally only.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void leaveChat() {
        if (!isChatActive) {
            JOptionPane.showMessageDialog(this, "You have already left the chat.");
            return;
        }

        try {
            if (chatService != null) {
                try {
                    if (callback != null && callbackId != null) {
                        chatService.unregisterCallbackById(callbackId);
                    }
                    chatService.notifyUserLeft(user.getNickname());
                } catch (Exception e) {
                    System.err.println("Error notifying server about user leaving: " + e.getMessage());
                }
            }

            displayMessage("❌ " + user.getNickname() + " left the chat");

            if (connectedUsers.size() <= 1) {
                LocalDateTime endTime = LocalDateTime.now();
                displayMessage("🔔 Chat stopped at: " + formatDateTime(endTime));
                saveChatToFile(endTime);
            }
        } catch (Exception e) {
            System.err.println("Error during chat leaving process: " + e.getMessage());
        } finally {
            if (subscriptionService != null && observer != null) {
                subscriptionService.removeObserver(observer);
            }

            isChatActive = false;
            JOptionPane.showMessageDialog(this, "You have left the chat.");
            dispose();
        }
    }

    private void saveChatToFile(LocalDateTime endTime) {
        try {
            String chatContent = chatArea.getText();
            services.ChatService chatService = new services.ChatService();
            if (currentChat != null) {
                currentChat.setEndTime(endTime);
                chatService.stopChat(currentChat, chatContent);
            } else {
                System.err.println("Current chat is null, cannot save chat log.");
            }

        } catch (Exception e) {
            System.err.println("Error saving chat to file: " + e.getMessage());
        }
    }

    public void userSubscribed(User user) {
        if (!user.getNickname().equals(this.user.getNickname())) {
            if (!connectedUsers.contains(user.getNickname())) {
                connectedUsers.add(user.getNickname());
            }
            displayMessage("✅ " + user.getNickname() + " has subscribed to this chat");
        }
    }

    public void userUnsubscribed(User user) {
        connectedUsers.remove(user.getNickname());
        displayMessage("❌ " + user.getNickname() + " has unsubscribed from this chat");
    }

    private void populateChatsDropdown() {
        try {
            List<models.Chat> allChats = subscriptionService.getAllChats();
            chatComboBox.removeAllItems();

            for (models.Chat chat : allChats) {
                chatComboBox.addItem(chat); // Add Chat object directly
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load chats: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private List<models.Chat> getAllChats() {
        try (Session session = utils.HibernateUtil.getSessionFactory().openSession()) {
            Query<models.Chat> query = session.createQuery("FROM Chat", models.Chat.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error getting chats: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void subscribeToChat() {
        models.Chat chat = (models.Chat) chatComboBox.getSelectedItem();

        if (chat != null) {
            boolean alreadySubscribed = subscriptionService.isUserSubscribedToChat(user, chat);
            boolean isCurrentChat = (currentChat != null && currentChat.getId() == chat.getId());

            if (!alreadySubscribed) {
                subscriptionService.subscribeUserToChat(user, chat);
                JOptionPane.showMessageDialog(this, "✅ You have subscribed to " + chat.getTitle());
                displayMessage("🔔 You are now viewing " + chat.getTitle() + ". Only subscribers can send messages to this chat.");
                currentChat = chat;
            } else if (!isCurrentChat) {
                currentChat = chat;
                displayMessage("🔔 You are now viewing " + chat.getTitle() + ".");
            } else {
                JOptionPane.showMessageDialog(this, "ℹ️ You are already subscribed to and viewing " + chat.getTitle());
            }
        }

    }

    private void unsubscribeFromChat() {
        models.Chat chat = (models.Chat) chatComboBox.getSelectedItem();

        if (chat != null) {
            boolean alreadySubscribed = subscriptionService.isUserSubscribedToChat(user, chat);

            if (alreadySubscribed) {
                subscriptionService.unsubscribeUserFromChat(user, chat);
                JOptionPane.showMessageDialog(this, "🚫 You have unsubscribed from " + chat.getTitle());
                displayMessage("🔕 You are no longer subscribed to " + chat.getTitle());

                // Optional: reset current chat if unsubscribed from it
                if (currentChat != null && currentChat.getId() == chat.getId()) {
                    currentChat = null;
                }

            } else {
                JOptionPane.showMessageDialog(this, "ℹ️ You are not subscribed to " + chat.getTitle());
            }
        }
    }

    private models.Chat getChatById(int chatId) {
        try (Session session = utils.HibernateUtil.getSessionFactory().openSession()) {
            return session.get(models.Chat.class, chatId);
        } catch (Exception e) {
            System.err.println("Error getting chat by ID: " + e.getMessage());
            return null;
        }
    }

    private void updateCurrentChatFromComboBox() {
        Object selectedItem = chatComboBox.getSelectedItem();
        if (selectedItem instanceof models.Chat) {
            currentChat = (models.Chat) selectedItem;
        } else {
            currentChat = null;
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        return dateTime.format(formatter);
    }

    private void openNewChatWindow() {
        try {
            ChatWindow newWindow = new ChatWindow(user);

            if (currentChat != null) {
                for (int i = 0; i < newWindow.chatComboBox.getItemCount(); i++) {
                    String item = newWindow.chatComboBox.getItemAt(i).toString();
                    int chatId = Integer.parseInt(item.replace("Chat ", ""));
                    if (chatId == currentChat.getId()) {
                        newWindow.chatComboBox.setSelectedIndex(i);
                        newWindow.subscribeToChat();
                        break;
                    }
                }
            }

            newWindow.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to open new chat window: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }


    }
}