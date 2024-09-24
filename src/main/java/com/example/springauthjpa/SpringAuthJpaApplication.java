package com.example.springauthjpa;

import com.example.springauthjpa.model.Post;
import com.example.springauthjpa.model.User;
import com.example.springauthjpa.repository.PostRepository;
import com.example.springauthjpa.repository.UserRepository;
import com.example.springauthjpa.service.MyUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringAuthJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(MyUserDetailsService myUserDetailsService, PostRepository posts, PasswordEncoder encoder) {
        return args -> {
            List<GrantedAuthority> authorities;
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            myUserDetailsService.createGroup("Users", authorities);

            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            myUserDetailsService.createGroup("Admins", authorities);

            User admin = new User("admin", encoder.encode("password"),
                    "FirstA", "LastA", "admin@bbb.ccc",
                    true, false, false, false);
            myUserDetailsService.createUser(admin);

            User user = new User("user", encoder.encode("password"),
                    "FirstU", "LastU", "user@bbb.ccc",
                    true, false, false, false);
            myUserDetailsService.createUser(user);

            User user2 = new User("user2", encoder.encode("password"),
                    "FirstU2", "LastU2", "user2@bbb.ccc",
                    true, false, false, false);
            myUserDetailsService.createUser(user2);

            myUserDetailsService.deleteUser(user);
            myUserDetailsService.createUser(user);

            myUserDetailsService.addUserToGroup("user", "Users");
            myUserDetailsService.addUserToGroup("user2", "Users");
            myUserDetailsService.addUserToGroup("admin", "Admins");

            myUserDetailsService.removeUserFromGroup("user2", "Users");

            user.setFirstname("New First Name");
            myUserDetailsService.updateUser(user);

            posts.save(new Post("Hello, World", "hello-world", "Welcome to my blog!", "test0001"));
        };

    }

}
