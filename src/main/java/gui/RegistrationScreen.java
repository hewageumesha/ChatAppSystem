package gui;

import models.User;
import services.UserService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class RegistrationScreen extends JFrame {

    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nicknameField;
    private JButton registerButton;
    private JButton uploadPictureButton;
    private JButton backButton;
    private String profilePicturePath;

    // Modern color scheme
    private static final Color HEADER_COLOR = new Color(65, 105, 225); // Royal Blue
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Alice Blue
    private static final Color BUTTON_COLOR = new Color(60, 179, 113); // Medium Sea Green
    private static final Color PIC_BUTTON_COLOR = new Color(100, 149, 237); // Cornflower Blue

    public RegistrationScreen() {
        setTitle("ChatNest - Register");
        setSize(400, 500); // Adjusted size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 💙 Header
        JLabel headerLabel = new JLabel("Create Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(HEADER_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // 📋 Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = createTextField();
        formPanel.add(emailField, gbc);

        // Username field
        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(createLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = createTextField();
        formPanel.add(usernameField, gbc);

        // Password field
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(createLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = createPasswordField();
        formPanel.add(passwordField, gbc);

        // Nickname field
        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(createLabel("Nickname:"), gbc);
        gbc.gridx = 1;
        nicknameField = createTextField();
        formPanel.add(nicknameField, gbc);

        // Profile picture button
        gbc.gridy = 4; gbc.gridx = 0;
        formPanel.add(createLabel("Profile Picture:"), gbc);
        gbc.gridx = 1;
        uploadPictureButton = createStyledButton("Choose Picture", PIC_BUTTON_COLOR);
        formPanel.add(uploadPictureButton, gbc);

        // Register button
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        registerButton = createStyledButton("Register", BUTTON_COLOR);
        formPanel.add(registerButton, gbc);

        // Back button
        gbc.gridy = 6;
        backButton = createStyledButton("Back to Login", PIC_BUTTON_COLOR);
        formPanel.add(backButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button actions
        setupActionListeners();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return field;
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

    private void setupActionListeners() {
        UserService userService = new UserService();

        uploadPictureButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Profile Picture");
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "gif"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                profilePicturePath = selectedFile.getAbsolutePath();
                JOptionPane.showMessageDialog(this,
                        "Selected: " + selectedFile.getName(),
                        "Profile Picture", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String nickname = nicknameField.getText().trim();

            // Empty field check
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Email format validation
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
                JOptionPane.showMessageDialog(this,
                        "Invalid email format",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Password strength validation
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "Password must be at least 6 characters long",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Username uniqueness check
            if (userService.isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this,
                        "Username already taken. Please choose another one.",
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create and register user
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setNickname(nickname);
            user.setProfilePicture(profilePicturePath);

            userService.registerUser(user);

            JOptionPane.showMessageDialog(this,
                    "🎉 Registration successful!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        backButton.addActionListener(e -> dispose());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationScreen().setVisible(true));
    }
}