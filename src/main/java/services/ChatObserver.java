package services;

import models.Chat;
import models.User;

public interface ChatObserver {
    void onSubscribe(User user, Chat chat);
    void onUnsubscribe(User user, Chat chat);
}