package network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.rmi.registry.LocateRegistry.createRegistry;

public class ChatServer {
    public static void main(String[] args) {
        try {
            // Create an instance of the server implementation
            ChatService chatService = new ChatServerImpl();

            // Start the RMI registry on port 1099
            Registry registry = createRegistry(1099);

            // Bind the service to the registry
            registry.rebind("ChatService", chatService);

            System.out.println("Chat server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
