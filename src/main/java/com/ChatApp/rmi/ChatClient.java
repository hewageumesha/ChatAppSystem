package com.ChatApp.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {

    void receiveMessage(String message) throws RemoteException;

    boolean isSubscribed(int chatId) throws RemoteException;

    void notifyChatStarted(String chatTitle, int chatId) throws RemoteException ;

    void notifyUserJoined(String nickName) throws RemoteException;

    void notifyUserLeft(String nickName) throws RemoteException;

    void receiveMessage(String nickName, String message) throws  RemoteException;
}

