package gui;

import models.User;
import services.UserService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class AdminProfileUpdateScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nicknameField;
    private JButton choosePicButton;
    private JButton updateButton;
    private JButton backButton;
    private byte[] profilePic = null;
    private String profilePicturePath;
    private User user;

    public AdminProfileUpdateScreen(User user) {
        this.user = user;

        // Basic frame setup
        setTitle("Admin - Edit User Profile");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and customize the header
        createHeader();

        // Create the form panel
        createFormPanel();

        // Create the button panel
        createButtonPanel();

        // Set visible at the end
        setVisible(true);
    }

    private void createHeader() {
        // Header with modern styling
        JLabel headerLabel = new JLabel("Update User Profile", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(70, 130, 180)); // SteelBlue
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);
    }

    private void createFormPanel() {
        // Form panel with GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245)); // Light gray background
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize form fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        nicknameField = new JTextField(20);
        choosePicButton = new JButton("Change Profile Picture");

        // Pre-fill fields with user data
        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            nicknameField.setText(user.getNickname());
            profilePicturePath = user.getProfilePicture();
        }

        // Style the components
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField.setFont(labelFont);
        formPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField.setFont(labelFont);
        formPanel.add(passwordField, gbc);

        // Nickname field
        JLabel nicknameLabel = new JLabel("Nickname:");
        nicknameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(nicknameLabel, gbc);

        gbc.gridx = 1;
        nicknameField.setFont(labelFont);
        formPanel.add(nicknameField, gbc);

        // Profile picture button
        JLabel profilePicLabel = new JLabel("Profile Picture:");
        profilePicLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(profilePicLabel, gbc);

        gbc.gridx = 1;
        choosePicButton.setBackground(new Color(100, 149, 237)); // CornflowerBlue
        choosePicButton.setForeground(Color.WHITE);
        choosePicButton.setFocusPainted(false);
        choosePicButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(choosePicButton, gbc);

        // Set up the profile picture selection functionality
        choosePicButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    profilePic = Files.readAllBytes(file.toPath());
                    profilePicturePath = file.getAbsolutePath();
                    JOptionPane.showMessageDialog(this, "Profile picture selected successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load picture!");
                    ex.printStackTrace();
                }
            }
        });

        add(formPanel, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        // Button panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Update button
        updateButton = new JButton("Update Profile");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setBackground(new Color(60, 179, 113)); // MediumSeaGreen
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Back button
        backButton = new JButton("Back to Admin Panel");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(70, 130, 180)); // SteelBlue
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        // Update button action
        updateButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String nickname = nicknameField.getText();

            // Validate input
            if (username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                JOptionPane.showMessageDialog(AdminProfileUpdateScreen.this,
                        "Please fill in all required fields (Username, Password, Nickname).",
                        "Update Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update user details
            user.setUsername(username);
            user.setPassword(password);
            user.setNickname(nickname);

            // If new profile picture was selected
            if (profilePic != null) {
                user.setProfilePicture(profilePicturePath);
                // In your actual implementation, you might need to store the byte array itself
                // depending on how your User class is structured
            }

            UserService userService = new UserService();
            boolean success = userService.updateUser(user);

            if (success) {
                JOptionPane.showMessageDialog(AdminProfileUpdateScreen.this, "✅ Profile updated successfully!");
                dispose(); // Close profile update screen
                new AdminPanel().setVisible(true); // Return to admin panel
            } else {
                JOptionPane.showMessageDialog(AdminProfileUpdateScreen.this, "✅ Profile updated successfully!");
            }
        });

        // Back button action
        backButton.addActionListener(e -> {
            dispose(); // Close profile update screen
            new AdminPanel().setVisible(true); // Return to admin panel
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }
}