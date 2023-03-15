package com.example.backend;

import com.example.backend.service.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        new UserServiceImpl();

        SpringApplication.run(BackendApplication.class, args);
    }
}
