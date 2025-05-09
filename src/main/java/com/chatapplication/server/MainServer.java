package com.chatapplication.server;

import com.chatapplication.server.service.ChatServerImpl;
import com.chatapplication.rmi.ChatService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MainServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Start RMI registry
            ChatService chatService = new ChatServerImpl();
            Naming.rebind("ChatService", chatService);
            System.out.println("✅ RMI ChatService is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
