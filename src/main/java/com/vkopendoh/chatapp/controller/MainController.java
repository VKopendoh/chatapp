package com.vkopendoh.chatapp.controller;

import com.vkopendoh.chatapp.model.Message;
import com.vkopendoh.chatapp.repository.CrudMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {


    @Autowired
    private CrudMessageRepository repository;

    @Value("${chatapp.messages.recent}")
    private int messagesLimit;

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }
        List<Message> messages = new ArrayList<>(repository
                .findAll(PageRequest.of(0, messagesLimit, Sort.by(Sort.Direction.DESC, "dateTime")))
                .getContent());
        Collections.reverse(messages);
        model.addAttribute("messages", messages);
        model.addAttribute("username", username);

        return "chat";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String username) {
        username = username.trim();

        if (username.isEmpty()) {
            return "login";
        }
        request.getSession().setAttribute("username", username);

        return "redirect:/";
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession(true).invalidate();
        String username = (String) request.getSession().getAttribute("username");

        return "redirect:/login";
    }
}
