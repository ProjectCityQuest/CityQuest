package com.example.backend.config;

import com.example.backend.db.DatabaseAccessImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// this configuration effectively disables CORS; this is helpful during development but a bad idea in production
// TODO: adjust CORS mapping to not allow everything
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  public Environment env;

  @Value("${spring.datasource.url}")
  public String DB_URL;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    DatabaseAccessImplementation.DB_URL = DB_URL;
    registry.addMapping("/**").allowedMethods("GET", "POST", "OPTIONS", "HEAD", "DELETE", "PUT", "PATCH");
  }
}