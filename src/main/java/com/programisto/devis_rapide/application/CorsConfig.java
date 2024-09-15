package com.programisto.devis_rapide.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${app.url}")
    String appUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/devis/**")
                .allowedOrigins(appUrl)
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

