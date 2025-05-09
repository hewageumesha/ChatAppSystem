package com.chatapplication.observer;

import com.chatapplication.model.Chat;
import com.chatapplication.model.User;

public interface ChatObserver {
    void onSubscribe(User user, Chat chat);
    void onUnsubscribe(User user, Chat chat);
}
