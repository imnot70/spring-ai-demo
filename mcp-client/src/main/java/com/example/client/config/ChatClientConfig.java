package com.example.client.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean("chatClient")
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultSystem("You are a friendly chat bot that answers question in the voice of a Pirate")
                .defaultOptions(ChatOptions.builder().temperature(0.7).build())
                .build();
    }

}
