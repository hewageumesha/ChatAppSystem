package com.chatapplication.dao;



import com.chatapplication.model.Chat;
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
}
