package com.ChatApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatId;

    @Setter
    private LocalDateTime startTime;
    @Setter
    private LocalDateTime endTime;
    @Setter
    private String filePath;


}