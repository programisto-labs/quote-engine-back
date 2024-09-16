package com.programisto.devis_rapide.application.discord_manager.service.impl;

import com.programisto.devis_rapide.application.discord_manager.entity.WebhookMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

@Service
public class DiscordWebhookService implements com.programisto.devis_rapide.application.discord_manager.service.DiscordWebhookService {

    @Value("${app.discord.webhook}")
    String webhookUrl;

    public void sendMessage(WebhookMessageRequest content) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = String.format("{\"content\": \"%s\",\"embeds\": [{\"description\": \"%s\"}]}", content.getContent(), content.getEmbeds());
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.exchange(webhookUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Discord message sent successfully.");
        } else {
            System.err.println("Error sending message: " + response.getStatusCode());
        }
    }
}

