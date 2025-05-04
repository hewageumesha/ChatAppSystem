package com.chatapplication.dao;


import com.chatapplication.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class UserDAO {

    public void save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public User getByEmailAndPassword(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE email = :email AND password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    public void deleteUser(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) session.remove(user);
            tx.commit();
        }
    }

    public boolean update(User loggedInUser) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(loggedInUser);  // Hibernate will update the user in DB
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
