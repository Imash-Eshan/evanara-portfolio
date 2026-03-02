package org.example.evanaraportfolio.config;

import lombok.RequiredArgsConstructor;
import org.example.evanaraportfolio.user.Role;
import org.example.evanaraportfolio.user.User;
import org.example.evanaraportfolio.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner runner(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                repo.save(User.builder()
                        .username("admin")
                        .password(encoder.encode("1234"))
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }
}