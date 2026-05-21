package com.example.smart_gate.controller; // <-- Тут должен быть ваш реальный package проекта

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Этот метод перехватывает запрос на самый главный адрес сайта "/"
    @GetMapping("/")
    public String redirectToLogin() {
        // И автоматически перенаправляет пользователя на страницу логина
        return "redirect:/login.html";
    }
}