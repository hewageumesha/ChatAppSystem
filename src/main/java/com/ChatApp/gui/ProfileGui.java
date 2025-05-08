package com.ChatApp.gui;



import com.ChatApp.dao.UserDAO;
import com.ChatApp.model.User;
import com.ChatApp.util.PasswordHasher;
import com.ChatApp.util.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.swing.*;
import java.awt.*;

public class ProfileGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nicknameField;
    private JTextField profilePicField;

    public ProfileGui() {
        super("👤 My Profile");
        User user = Session.loggedInUser;

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField(user.getUsername());
        passwordField = new JPasswordField();
        nicknameField = new JTextField(user.getNickname());
        profilePicField = new JTextField(user.getProfilePicture());

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Nickname:"));
        panel.add(nicknameField);
        panel.add(new JLabel("Profile Picture Path:"));
        panel.add(profilePicField);

        JButton updateBtn = new JButton("🔄 Update");
        updateBtn.addActionListener(e -> updateProfile(user));
        panel.add(updateBtn);

        add(panel);
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateProfile(User user) {
        try {
            user.setUsername(usernameField.getText().trim());
            String newPassword = new String(passwordField.getPassword()).trim();
            if (!newPassword.isEmpty()) {
                user.setPassword(PasswordHasher.hash(newPassword));
            }
            user.setNickname(nicknameField.getText().trim());
            user.setProfilePicture(profilePicField.getText().trim());

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            UserDAO userDao = new UserDAO(sessionFactory);
            userDao.saveUser(user); // You might create an updateUser() method for best practice

            JOptionPane.showMessageDialog(this, "✅ Profile updated successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error updating profile: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
