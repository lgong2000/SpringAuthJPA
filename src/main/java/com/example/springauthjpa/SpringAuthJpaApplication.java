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

@SpringBootApplication
public class SpringAuthJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users, PostRepository posts, PasswordEncoder encoder) {
        return args -> {
            User admin = new User("admin", encoder.encode("password"),
                    "FirstA", "LastA", "admin@bbb.ccc",
                    true, false, false, false);
            users.save(admin);
            User user = new User("user", encoder.encode("password"),
                    "FirstU", "LastU", "user@bbb.ccc",
                    true, false, false, false);
            users.save(user);

            User user2 = new User("user2", encoder.encode("password"),
                    "FirstU2", "LastU2", "user2@bbb.ccc",
                    true, false, false, false);
            users.save(user2);

            users.delete(user);
            users.save(user);

            posts.save(new Post("Hello, World", "hello-world", "Welcome to my blog!", "test0001"));
        };

    }

}
