package com.giancarlos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig is a configuration class that sets up Cross-Origin Resource Sharing (CORS) mappings for the application.
 * It implements the `WebMvcConfigurer` interface to customize Spring MVC configuration, specifically to allow
 * cross-origin requests from a specified frontend domain.
 *
 * This configuration:
 * 1. Allows CORS for all endpoints (`/**`).
 * 2. Permits requests from `http://localhost:5173` (typically the frontend server during development).
 * 3. Specifies allowed HTTP methods: GET, POST, PUT, DELETE, and OPTIONS.
 * 4. Allows the `Authorization` and `Content-Type` headers in requests.
 * 5. Enables the use of credentials (cookies, authorization headers, etc.) in cross-origin requests.
 *
 * @Configuration
 * @see WebMvcConfigurer
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }
}
