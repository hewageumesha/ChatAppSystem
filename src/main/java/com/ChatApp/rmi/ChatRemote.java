package com.ChatApp.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatRemote extends Remote {
    void createChat(String chatId) throws RemoteException;
    void subscribeUser(int userId, String chatId) throws RemoteException;
    void unsubscribeUser(int userId, String chatId) throws RemoteException;
}
