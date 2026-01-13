package com.saraai.controller;

import com.saraai.service.OpenAIService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // later restrict to your GitHub Pages domain
public class ChatController {

  private final OpenAIService openAIService;

  public ChatController(OpenAIService s){ this.openAIService = s; }

  @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Map<String,String>> chat(@RequestBody Map<String,String> body){
    String prompt = body.get("prompt");
    // returns Mono<Map<String,String>> with key 'text'
    return openAIService.createChatCompletion(prompt)
        .map(text -> Map.of("text", text));
  }
}
