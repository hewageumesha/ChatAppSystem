package com.chatapplication.dao;

import com.chatapplication.model.Chat;
import com.chatapplication.model.User;
import com.chatapplication.dao.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatDAO {

    // Save a new Chat
    public void save(Chat chat) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(chat);
            tx.commit();
        }
    }

    // Retrieve all Chats
    public List<Chat> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Chat", Chat.class).list();
        }
    }

    // Delete a Chat by its ID
    public void deleteChat(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Chat chat = session.get(Chat.class, id);
            if (chat != null) {
                session.remove(chat);
            }
            tx.commit();
        }
    }

    // Log chat file after the chat ends and save to DB
    public void logChatFile(int chatId, String filePath) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Chat chat = session.get(Chat.class, chatId);
            if (chat != null) {
                chat.setEndTime(new Date());
                chat.setFilePath(filePath);
                session.merge(chat);
            }
            tx.commit();
        }
    }

    // Subscribe a user to a chat
    public void subscribeUserToChat(int userId, int chatId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            Chat chat = session.get(Chat.class, chatId);
            if (user != null && chat != null) {
                // Implement logic to add user to the chat's subscription list
                chat.getUsers().add(user); // Assuming a 'users' collection in the Chat class
                user.getChats().add(chat); // Assuming a 'chats' collection in the User class
                session.merge(chat);
                session.merge(user);
            }
            transaction.commit();
        }
    }

    // Unsubscribe a user from a chat
    public void unsubscribeUserFromChat(int userId, int chatId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            Chat chat = session.get(Chat.class, chatId);
            if (user != null && chat != null) {
                // Implement logic to remove user from chat's subscription list
                chat.getUsers().remove(user);
                user.getChats().remove(chat);
                session.merge(chat);
                session.merge(user);
            }
            transaction.commit();
        }
    }

    // Retrieve all chats for a specific user
    public List<Chat> getChatsForUser(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, userId);
            if (user != null) {
                return new ArrayList<>(user.getChats());
                // Assuming User has a 'chats' collection
            }
            return null;
        }
    }

    // Retrieve a specific chat by its ID
    public Chat getChatById(int chatId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Chat.class, chatId);
        }
    }
}
