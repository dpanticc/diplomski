package com.dusanpan.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        String[] allowDomains = new String[2];
        allowDomains[0] = "http://localhost:4200";
        allowDomains[1] = "http://localhost:8080";

        System.out.println("CORS configuration....");

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Adjust the mapping path based on your API endpoints
                        .allowedOrigins(allowDomains) // Replace with the origin of your Angular app
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .allowedHeaders("*") // Allow all headers, including "Authorization"
                        .allowCredentials(true);
            }
        };
    }
}
