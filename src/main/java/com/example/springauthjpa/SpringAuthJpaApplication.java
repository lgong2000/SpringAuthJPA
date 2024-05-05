package com.example.springauthjpa;

import com.example.springauthjpa.model.Post;
import com.example.springauthjpa.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringAuthJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PostRepository posts) {
        return args -> {
            posts.save(new Post("Hello, World", "hello-world", "Welcome to my blog!", "test0001"));
        };

    }

}
