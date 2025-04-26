package com.chatapplication.client;

import com.chatapplication.client.gui.*;
import com.chatapplication.model.User;
import org.hibernate.service.NullServiceException;

import java.rmi.RemoteException;

public class MainClient {
    public static void main(String[] args) throws RemoteException {

        //new  ChatWindow("maxi",1);
        new MainPanel();
//        User User = null;
//        new ViewAllChatsForm(User);



    }
}
