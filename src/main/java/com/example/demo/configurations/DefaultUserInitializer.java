package com.example.demo.configurations;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserDetail;
import com.example.demo.repositories.jpa.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DefaultUserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!userRepository.existsByUsername("superuser")) {
            User defaultUser = new User();
            UserDetail defaultUserDetail = new UserDetail();
            defaultUser.setUsername("superuser");
            defaultUser.setPassword(passwordEncoder.encode("password"));
            defaultUser.setRole(Role.ADMIN);
            defaultUserDetail.setFirstName("Luis");
            defaultUserDetail.setLastName("Oropeza");
            defaultUserDetail.setUser(defaultUser);
            defaultUser.setUserDetail(defaultUserDetail);
            userRepository.save(defaultUser);
        }
    }
}