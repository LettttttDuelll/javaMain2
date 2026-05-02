package com.example.laptopshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.laptopshop.entity.Laptop;
import com.example.laptopshop.repository.LaptopRepository;

import lombok.RequiredArgsConstructor;

import com.example.laptopshop.entity.Laptop;
import com.example.laptopshop.repository.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final LaptopRepository laptopRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public String ask(String userMessage) {

        List<Laptop> laptops = laptopRepository.searchByKeyword(extractKeyword(userMessage));

        StringBuilder context = new StringBuilder();
        context.append("Dữ liệu laptop trong cửa hàng:\n");

        for (Laptop l : laptops.stream().limit(5).toList()) {
            context.append("- ")
                   .append(l.getName())
                   .append(" | Giá: ")
                   .append(l.getCurrent_price())
                   .append(" VND\n");
        }

        String prompt = """
Bạn là nhân viên tư vấn laptop.

%s

Khách hỏi: %s

Hãy tư vấn ngắn gọn, đúng dữ liệu trên.
""".formatted(context, userMessage);

        return callGemini(prompt);
    }

    private String extractKeyword(String text) {
        text = text.toLowerCase();

        if (text.contains("asus")) return "asus";
        if (text.contains("acer")) return "acer";
        if (text.contains("dell")) return "dell";
        if (text.contains("lenovo")) return "lenovo";
        if (text.contains("hp")) return "hp";

        return "";
    }

    private String callGemini(String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
{
  "contents": [
    {
      "parts": [
        {
          "text": "%s"
        }
      ]
    }
  ]
}
""".formatted(prompt.replace("\"", "\\\""));

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        String url = apiUrl + "?key=" + apiKey;

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, entity, String.class);

        //return response.getBody();

        String json = response.getBody();

          String start = "\"text\": \"";
          int index = json.indexOf(start);

          if (index != -1) {
              int from = index + start.length();
              int to = json.indexOf("\"", from);
              return json.substring(from, to)
                      .replace("\\n", "\n")
                      .replace("\\\"", "\"");
          }

          return "Xin lỗi, hệ thống đang bận.";
    }
}
