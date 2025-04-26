package com.chatapplication.client;

import com.chatapplication.client.gui.*;

import java.rmi.RemoteException;

public class MainClient {
    public static void main(String[] args) throws RemoteException {

        //new  ChatWindow("maxi",1);
        new MainPanel();



    }
}
