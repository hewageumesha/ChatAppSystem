package com.chatapplication.client;

import com.chatapplication.client.gui.*;
import com.chatapplication.model.User;
import org.hibernate.service.NullServiceException;

import java.rmi.RemoteException;

public class MainClient {
    public static void main(String[] args) throws RemoteException {


        new MainPanel();
//        User User = null;
        //User User = null;
        //new ViewAllChatsForm(User);



    }
}
