package com.example.springauthjpa;

import com.example.springauthjpa.model.Post;
import com.example.springauthjpa.model.User;
import com.example.springauthjpa.repository.PostRepository;
import com.example.springauthjpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringAuthJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PostRepository posts, UserRepository users, PasswordEncoder encoder) {
        return args -> {
            users.save(new User("user", encoder.encode("password"), "ROLE_USER"));
            users.save(new User("admin", encoder.encode("password"), "ROLE_USER,ROLE_ADMIN"));
            posts.save(new Post("Hello, World", "hello-world", "Welcome to my blog!", "test0001"));
        };

    }

}
