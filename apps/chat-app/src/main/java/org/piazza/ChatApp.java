package org.piazza;

import org.piazza.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ChatApp implements CommandLineRunner {
    static ChatService chatService;
    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public void run(String... strings) {
        Object object = applicationContext.getBean("chatService");
        this.chatService = (ChatService) object;
    }
    public static void main(String[] args) throws IOException {
       SpringApplication.run(ChatApp.class, args);
    }
}