package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

//@EnableWebFlux
@EnableAutoConfiguration
@SpringBootApplication
public class ClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }

}
