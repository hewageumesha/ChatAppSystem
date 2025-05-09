package com.chatapplication.server;



import com.chatapplication.rmi.ChatService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerLauncher {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ChatService chatService = new ChatServiceImpl();
            Naming.rebind("ChatService", chatService);
            System.out.println("Server running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
