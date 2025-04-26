package com.chatapplication.client.gui;


import com.chatapplication.model.Chat;
import com.chatapplication.model.User;
import com.chatapplication.rmi.ChatClient;
import com.chatapplication.rmi.ChatService;
import jakarta.persistence.EntityManager;
import com.chatapplication.dao.HibernateUtil;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewAllChatsForm extends JFrame {
    private User loggedInUser;
    private JTable chatTable;
    private JButton subscribeButton;
    private JButton unsubscribeButton;
    private JLabel statusBar;
    private ChatService chatService;
    private final Set<ChatClient> clients = new HashSet<>();
    private List<Chat> chats = new ArrayList<>();

    public ViewAllChatsForm(User user) throws RemoteException {
        this.loggedInUser = user;
        this.chatService = new ChatService() {
            @Override
            public void registerClient(ChatClient client) throws RemoteException {
                clients.add(client);
                System.out.println("Client registered: " + client);
            }

            @Override
            public void unregisterClient(ChatClient client) throws RemoteException {
                clients.remove(client);
                System.out.println("Client unregistered: " + client);
            }

            @Override
            public void startChat(String chatTitle, int chatId) throws RemoteException {
                for (ChatClient client : clients) {
                    client.notifyChatStarted(chatTitle, chatId); // assuming you have a notifyChatStarted() method in ChatClient
                }
            }

            @Override
            public void sendMessage(String nickName, String message) throws RemoteException {
                for (ChatClient client : clients) {
                    client.receiveMessage(nickName, message); // assuming you have a receiveMessage() method in ChatClient
                }
            }

            @Override
            public void userJoin(String nickName) throws RemoteException {
                for (ChatClient client : clients) {
                    client.notifyUserJoined(nickName); // assuming you have a notifyUserJoined() method in ChatClient
                }
            }

            @Override
            public void userLeave(String nickName) throws RemoteException {
                for (ChatClient client : clients) {
                    client.notifyUserLeft(nickName); // assuming you have a notifyUserLeft() method in ChatClient
                }
            }

            @Override
            public List<Chat> getAllChats() {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try {
                    return session.createQuery("FROM Chat", Chat.class).list();
                } finally {
                    session.close();
                }
            }
        };

        setTitle("View All Chats");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Chat ID", "Chat Title", "Start Time", "End Time"};
        Object[][] data = fetchChatData();
        chatTable = new JTable(data, columnNames);
        chatTable.setFillsViewportHeight(true);
        chatTable.setRowHeight(25);
        chatTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        chatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane tableScrollPane = new JScrollPane(chatTable);

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

        statusBar = new JLabel("Select a chat to subscribe/unsubscribe.");
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setBackground(new Color(220, 220, 220));
        statusBar.setOpaque(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(250, 250, 250));
        mainPanel.add(new JLabel("  All Available Chats", SwingConstants.LEFT), BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);

        subscribeButton.addActionListener(e -> {
            int selectedRow = chatTable.getSelectedRow();
            if (selectedRow >= 0) {
                String chatTitle = chatTable.getValueAt(selectedRow, 1).toString();
                statusBar.setText("Subscribed to " + chatTitle);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a chat to subscribe.");
            }
        });

        unsubscribeButton.addActionListener(e -> {
            int selectedRow = chatTable.getSelectedRow();
            if (selectedRow >= 0) {
                String chatTitle = chatTable.getValueAt(selectedRow, 1).toString();
                statusBar.setText("Unsubscribed from " + chatTitle);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a chat to unsubscribe.");
            }
        });
    }

    private Object[][] fetchChatData() throws RemoteException {
        List<Chat> chatList = chatService.getAllChats();
        Object[][] data = new Object[chatList.size()][4];

        for (int i = 0; i < chatList.size(); i++) {
            Chat chat = chatList.get(i);
            data[i][0] = chat.getId();
            data[i][1] = chat.getTitle();
            data[i][2] = chat.getStartTime();
            data[i][3] = chat.getEndTime();
        }

        return data;
    }
}
