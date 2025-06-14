package com.example.client.controller;

import com.example.client.beans.ActorFilms;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("chat")
@RestController
public class ChatDemoController {

    private static final Logger LOG = LoggerFactory.getLogger(ChatDemoController.class);

    @Autowired
    private OllamaChatModel chatModel;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private ChatMemory chatMemory;

    public ChatDemoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        objectMapper = new ObjectMapper();
    }

    @GetMapping("/ai/multimodal")
    public String multimodal(){
        String text = "Explain what do you see in this picture?";
        FileSystemResource imageResource = new FileSystemResource("C:\\Users\\zhangpeng\\Downloads\\multimodal.test.png");
        List<Media> medias = new ArrayList<>();
        medias.add(new Media(MimeTypeUtils.IMAGE_PNG, imageResource));
        UserMessage userMessage = UserMessage.builder()
                .text(text).media(medias).build();
//        ChatResponse resp = chatModel.call(new Prompt(userMessage));
        ChatResponse resp = ChatClient.create(chatModel)
                .prompt(new Prompt(userMessage))
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .chatResponse();
        return resp.getResult().getOutput().getText();
    }

    @GetMapping("/ai/logging")
    public String logging(){
        ChatResponse response = ChatClient.create(chatModel).prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user("Tell me a joke?")
                .call()
                .chatResponse();
        return response.getResult().getOutput().getText();
    }

    @GetMapping("/ai/simple")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("completion", chatClient.prompt().user(message).call().content());
    }

    @GetMapping("/ai")
    String generation(@RequestParam("userInput") String userInput) {
        long start = System.currentTimeMillis();
        String content = this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
        LOG.info("generation, cost time:{}", (System.currentTimeMillis() - start));
        return content;
    }

    @GetMapping("/films")
    String films() throws JsonProcessingException {
        long start = System.currentTimeMillis();
        List<ActorFilms> actorFilms = chatClient.prompt()
                .user("Generate the filmography of 3 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {
                });
        LOG.info("films, cost time:{}", (System.currentTimeMillis() - start));
        return objectMapper.writeValueAsString(actorFilms);
    }

    @GetMapping("/film")
    String film() throws JsonProcessingException {
        long start = System.currentTimeMillis();
        ActorFilms actorFilms = chatClient.prompt()
                .user("Generate the filmography for a random actor.")
                .call()
                .entity(ActorFilms.class);
        LOG.info("film, cost time:{}", (System.currentTimeMillis() - start));
        return objectMapper.writeValueAsString(actorFilms);
    }

    @GetMapping(value = "/stream/joke")
    Flux<String> streamFilms() {
        long start = System.currentTimeMillis();
        Flux<String> output = chatClient.prompt()
                .user("Tell me a joke")
                .stream()
                .content();
        LOG.info("streamJoke, cost time:{}", (System.currentTimeMillis() - start));
        return output;
    }

    @GetMapping(value = "/stream/filmography")
    Flux<List<ActorFilms>> streamFilmsConstruct() {
        BeanOutputConverter<List<ActorFilms>> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorFilms>>() {
        });

        long start = System.currentTimeMillis();
        Flux<String> flux = this.chatClient.prompt()
                .user(u -> u.text("""
                        Generate the filmography for a random actor.
                        {format}
                      """)
                        .param("format", converter.getFormat()))
                .stream()
                .content();
        LOG.info("streamFilmsConstruct, cost time:{}", (System.currentTimeMillis() - start));
        String content = flux.collectList().block().stream().collect(Collectors.joining());
        List<ActorFilms> actorFilms = converter.convert(content);
        return Flux.just(actorFilms);
    }
}
