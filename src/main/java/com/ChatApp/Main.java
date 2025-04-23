package com.ChatApp;

import org.hibernate.cfg.Configuration;

import com.ChatApp.client.AdminPanel;

import org.hibernate.SessionFactory;
public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        new AdminPanel(sessionFactory);
    }
}