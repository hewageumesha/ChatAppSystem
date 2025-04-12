package com.ChatApp.dao;

import com.ChatApp.model.Chat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ChatDao {
    private SessionFactory sessionFactory;

    public ChatDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveChat(Chat chat) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(chat);
        session.getTransaction().commit();
    }

    public List<Chat> getAllChats() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query<Chat> query = session.createQuery("FROM Chat", Chat.class);
        List<Chat> chats = query.getResultList();
        session.getTransaction().commit();
        return chats;
    }
}
