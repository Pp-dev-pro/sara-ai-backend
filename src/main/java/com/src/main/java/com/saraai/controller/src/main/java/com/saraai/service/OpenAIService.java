package com.saraai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

  private final WebClient client;
  @Value("${openai.api.key}")
  private String apiKey;

  public OpenAIService() {
    this.client = WebClient.builder()
        .baseUrl("https://api.openai.com")
        .build();
  }

  public Mono<String> createChatCompletion(String prompt) {
    Map<String,Object> body = Map.of(
        "model", "gpt-4o-mini",
        "messages", List.of(Map.of("role","user","content",prompt)),
        "max_tokens", 800
    );

    return client.post()
        .uri("/v1/chat/completions")
        .header("Authorization", "Bearer " + apiKey)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(Map.class)
        .map(resp -> {
          try {
            var choices = (List) resp.get("choices");
            var first = (Map) choices.get(0);
            var message = (Map) first.get("message");
            return (String) message.get("content");
          } catch (Exception e) {
            return "Error parsing OpenAI response: " + e.getMessage();
          }
        });
  }
}
