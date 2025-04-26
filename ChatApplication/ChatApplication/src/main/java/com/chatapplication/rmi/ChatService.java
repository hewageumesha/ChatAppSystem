package com.chatapplication.rmi;


import com.chatapplication.model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    void registerClient(ChatClient client) throws RemoteException;
    void unregisterClient(ChatClient client) throws RemoteException;
    void startChat(String chatTitle, int chatId) throws RemoteException;
    void sendMessage(String nickName, String message) throws RemoteException;
    void userJoin(String nickName) throws RemoteException;
    void userLeave(String nickName) throws RemoteException;
}
