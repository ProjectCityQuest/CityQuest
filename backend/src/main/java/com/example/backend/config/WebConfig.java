package com.example.backend.config;

import com.example.backend.db.DatabaseAccessImplementation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// this configuration effectively disables CORS; this is helpful during development but a bad idea in production
// TODO: adjust CORS mapping to not allow everything
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${spring.datasource.url}")
  public String DB_URL;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    DatabaseAccessImplementation.DB_URL = DB_URL;
    registry.addMapping("/**").allowedMethods("GET", "POST", "OPTIONS", "HEAD", "DELETE", "PUT", "PATCH");
  }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Match everything except files with an extension or API routes
        registry.addViewController("/{spring:[^.]*}")
                .setViewName("forward:/index.html");
        registry.addViewController("/**/{spring:[^.]*}")
                .setViewName("forward:/index.html");
    }
}