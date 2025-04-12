package com.ChatApp.rmi;

import com.ChatApp.dao.ChatDao;
import com.ChatApp.model.Chat;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServer implements ChatRemote {
    private ChatDao chatDao;

    public ChatServer() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().getSessionFactoryBuilder().build();
        chatDao = new ChatDao(sessionFactory);
    }

    @Override
    public void createChat(String chatId) throws RemoteException {
        Chat chat = new Chat();
        chat.setChatId(chatId);
        chatDao.saveChat(chat);
    }

    @Override
    public void subscribeUser(int userId, String chatId) throws RemoteException {
        // Implement subscribe logic
    }

    @Override
    public void unsubscribeUser(int userId, String chatId) throws RemoteException {
        // Implement unsubscribe logic
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1099);
        ChatServer server = new ChatServer();
        registry.bind("ChatServer", server);
        System.out.println("RMI Server started");
    }
}

