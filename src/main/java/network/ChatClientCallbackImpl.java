package network;

import gui.ChatWindow;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientCallbackImpl extends UnicastRemoteObject implements ChatClientCallback {

    private ChatWindow chatWindow;

    public ChatClientCallbackImpl(ChatWindow chatWindow) throws RemoteException {
        this.chatWindow = chatWindow;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        chatWindow.displayMessage(message);
    }

    @Override
    public void userJoined(String nickname) throws RemoteException {
        // The message will be displayed via receiveMessage
    }

    @Override
    public void userLeft(String nickname) throws RemoteException {
        // The message will be displayed via receiveMessage
    }
}
