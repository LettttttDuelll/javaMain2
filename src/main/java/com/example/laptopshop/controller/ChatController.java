package com.example.laptopshop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.laptopshop.dto.ChatRequest;
import com.example.laptopshop.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public Map<String, String> chat(@RequestBody ChatRequest request) {

        String reply = chatService.ask(request.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("reply", reply);

        return response;
    }
}
