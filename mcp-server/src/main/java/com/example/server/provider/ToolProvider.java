package com.example.server.provider;

import com.example.server.service.DemoService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ToolProvider {

    @Bean("demoTools")
    public ToolCallbackProvider demoTools(DemoService service){
        return MethodToolCallbackProvider.builder().toolObjects(service).build();
    }

}
