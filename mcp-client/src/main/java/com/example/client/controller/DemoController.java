package com.example.client.controller;

import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

//    @Autowired
//    private OllamaChatModel ollamaChatModel;
//    @Autowired
//    private ToolCallbackProvider toolCallbackProvider;
//
//    @GetMapping("/ai/generate")
//    public String generate(@RequestParam(value = "message", defaultValue = "推荐一个公众号") String message) {
//
//        ChatClient chatClient = ChatClient.builder(ollamaChatModel)
//                .defaultTools((Object) toolCallbackProvider.getToolCallbacks())
//                .build();
//        ChatClient.CallResponseSpec call = chatClient.prompt().user(message).call();
//        return call.content();
//    }
//
//    @Bean("toolCallbackProvider")
//    public ToolCallbackProvider toolCallbackProvider(){
//        return new AsyncMcpToolCallbackProvider();
//    }

}
