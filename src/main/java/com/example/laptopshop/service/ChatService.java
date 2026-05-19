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

      String keyword = extractKeyword(userMessage);

System.out.println("Keyword = [" + keyword + "]");


        List<Laptop> laptops = laptopRepository.searchByKeyword(keyword);
        System.out.println("Laptop found = " + laptops.size());

        StringBuilder context = new StringBuilder();
        context.append("Dữ liệu laptop trong cửa hàng:\n");
        System.out.println(context.toString());
        for (Laptop l : laptops.stream().limit(5).toList()) {
            context.append("- ")
       .append(l.getName())
       .append(" | Danh mục: ")
       .append(l.getCategory().getName())
       .append(" | Giá: ")
       .append(l.getCurrent_price())
       .append(" VND\n");
        }
      
        String prompt = """
Bạn là nhân viên tư vấn laptop của cửa hàng LaptopShop.

Chỉ sử dụng dữ liệu sản phẩm được cung cấp bên dưới.

Dữ liệu laptop:

%s

Câu hỏi khách hàng:
%s

Yêu cầu trả lời:
- Trả lời như con người, thân thiện, dễ hiểu. những câu hỏi xã giao như "cảm ơn", "chào bạn" thì cứ trả lời bình thường, không cần liên quan đến laptop.
- Tư vấn nhiều options nếu có thể, max = 4
- Nêu tên laptop phù hợp
- Giải thích vì sao phù hợp
""".formatted(context, userMessage);

        return callGemini(prompt);
    }

    private String extractKeyword(String text) {
        text = text.toLowerCase();

        String prompt = """
Trích xuất keyword tìm kiếm laptop từ câu hỏi.

Chỉ trả về keyword. keyword có thể là tên laptop, hoặc danh mục (gaming, office, macbook, đồ họa). chỉ gồm 1 từ thôi, ví dụ "gaming", "office", "macbook", "đồ họa", hoặc hãng laptop như "asus", "macbook", "dell", "lenovo", "hp", "msi". Nếu không tìm thấy keyword nào thì trả về empty string.
Không giải thích. ưu tiên keyword là tên laptop, nếu không có thì mới đến danh mục, nếu không có thì mới đến hãng.
Hãy hướng câu hỏi của khách hàng vào 1 trong 4 danh mục sau nếu có thể:

1 Gaming	gaming		 
2	Office	office		 
3	Macbook	macbook		 
4	Đồ họa	do-hoa

Câu hỏi:
%s
""".formatted(text);

    return callGemini(prompt).trim();
        
        //return "";
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
