package services;

import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import utils.HibernateUtil;

import java.util.List;

public class UserService {

    // Register a new user
    public void registerUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // 🔐 Hash password before saving
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            System.out.println("User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isUsernameTaken(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE username = :username";
            User existing = session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return existing != null;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Assume taken if error occurs
        }
    }


    // Login a user
    public User loginUser(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE email = :email";
            User user = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                System.out.println("Login successful!");
                return user;
            } else {
                System.out.println("Invalid email or password.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update user details
    public boolean updateUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            System.out.println("User updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all users
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get a user by ID
    public User getUserById(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, userId);
            if (user != null) {
                return user;
            } else {
                System.out.println("User not found with ID: " + userId);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Delete a user by ID
    public boolean deleteUser(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Use getUserById to get the user
            User user = getUserById(userId);

            if (user != null) {
                session.delete(user);
                System.out.println("User deleted successfully!");
                transaction.commit();
                return true;
            } else {
                System.out.println("User not found with ID: " + userId);
                transaction.commit();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a user by nickname
    public User getUserByNickname(String nickname) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE nickname = :nickname";
            User user = session.createQuery(hql, User.class)
                    .setParameter("nickname", nickname)
                    .uniqueResult();

            if (user != null) {
                return user;
            } else {
                System.out.println("User not found with nickname: " + nickname);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
