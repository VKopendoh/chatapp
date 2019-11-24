package com.vkopendoh.chatapp.controller;

import com.vkopendoh.chatapp.model.Message;
import com.vkopendoh.chatapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

import static com.vkopendoh.chatapp.controller.WebSocketEventListener.users;

@Controller
public class MessageController {
    private final MessageRepository repository;

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public MessageController(MessageRepository messageRepository, SimpMessageSendingOperations messagingTemplate) {
        this.repository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public Message sendMessage(@Payload Message chatMessage) {
        chatMessage.setDateTime(new Timestamp(System.currentTimeMillis()));
        repository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public Message addUser(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String user = chatMessage.getSender();
        users.add(user);
        messagingTemplate.convertAndSend("/topic/usersOnline", users);
        headerAccessor.getSessionAttributes().put("username", user);
        return chatMessage;
    }

}
