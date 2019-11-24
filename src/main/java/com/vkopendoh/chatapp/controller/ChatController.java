package com.vkopendoh.chatapp.controller;

import com.vkopendoh.chatapp.model.Message;
import com.vkopendoh.chatapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ChatController {
    private final MessageRepository messageRepository;

    @Autowired
    public ChatController(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Value("${chatapp.messages.recent}")
    private int messagesLimit;

    @RequestMapping("/chat")
    public String index(HttpServletRequest request, Model model, Principal principal) {
        String username = principal.getName(); //= (String) request.getSession().getAttribute("username");

       if (username.isEmpty()) {
            return "redirect:/login";
        }
        List<Message> messages = new ArrayList<>(messageRepository
                .findAll(PageRequest.of(0, messagesLimit, Sort.by(Sort.Direction.DESC, "dateTime")))
                .getContent());
        Collections.reverse(messages);
        model.addAttribute("messages", messages);
        model.addAttribute("username", username);
        return "chat";
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession(true).invalidate();
        return "redirect:/";
    }

}
