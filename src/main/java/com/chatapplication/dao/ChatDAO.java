package com.chatapplication.dao;

import com.chatapplication.model.Chat;
import com.chatapplication.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class ChatDAO {

    public void save(Chat chat) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(chat);
            tx.commit();
        }
    }

    public List<Chat> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Chat", Chat.class).list();
        }
    }

    public void deleteChat(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Chat chat = session.get(Chat.class, id);
            if (chat != null) session.remove(chat);
            tx.commit();
        }
    }

    public void logChatFile(int chatId, String filePath) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Chat chat = session.get(Chat.class, chatId);
            chat.setEndTime(new Date());
            chat.setFilePath(filePath);
            session.merge(chat);
            tx.commit();
        }
    }


    public List<Chat> getAllChats() {

        Transaction transaction = null;
        List<Chat> chatList = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<Chat> query = session.createQuery("from Chat", Chat.class);
            chatList = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return chatList;
    }

    /**
     * Subscribe a user to a chat - proper way to handle LazyInitializationException
     * @param chatId the ID of the chat
     * @param userId the ID of the user to subscribe
     * @return true if successful, false otherwise
     */
    public boolean subscribeUserToChat(int chatId, int userId) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Load entities within the session
            Chat chat = session.get(Chat.class, chatId);
            User user = session.get(User.class, userId);

            if (chat != null && user != null) {
                // Modify collection within the session
                chat.getUsers().add(user);
                session.update(chat);

                transaction.commit();
                return true;
            }

            transaction.commit();
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Unsubscribe a user from a chat - proper way to handle LazyInitializationException
     * @param chatId the ID of the chat
     * @param userId the ID of the user to unsubscribe
     * @return true if successful, false otherwise
     */
    public boolean unsubscribeUserFromChat(int chatId, int userId) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Load entities within the session
            Chat chat = session.get(Chat.class, chatId);
            User user = session.get(User.class, userId);

            if (chat != null && user != null) {
                // Modify collection within the session
                chat.getUsers().remove(user);
                session.update(chat);

                transaction.commit();
                return true;
            }

            transaction.commit();
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
