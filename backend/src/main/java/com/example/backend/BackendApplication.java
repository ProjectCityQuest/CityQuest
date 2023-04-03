/**
 * Main Class of the Backend Application using:
 * @software Springboot
 * @version 3.0.1
 *
 * Execute this file to start the Backend Application
 */

package com.example.backend;

import com.example.backend.service.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        new UserServiceImpl();
    }
}
