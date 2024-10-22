package com.programisto.devis_rapide.application.discord_manager.service.impl;

import com.programisto.devis_rapide.application.discord_manager.entity.WebhookMessageRequest;
import com.programisto.devis_rapide.application.discord_manager.entity.WebhookRawRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
public class DiscordWebhookService implements com.programisto.devis_rapide.application.discord_manager.service.DiscordWebhookService {

    public void sendMessage(WebhookMessageRequest content) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = String.format("{\"content\": \"%s\",\"embeds\": [{\"description\": \"%s\"}]}", content.getContent(), content.getEmbeds());
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.exchange(content.getWebhook(), HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Discord message sent successfully.");
        } else {
            System.err.println("Error sending message: " + response.getStatusCode());
        }
    }

    public void sendRawMessage(WebhookRawRequest content) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> jsonPayload = new HashMap<>();

        jsonPayload.put("devis", content.getDevis());
        jsonPayload.put("projet", content.getProjet());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(content.getWebhook(), HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Discord message sent successfully.");
        } else {
            System.err.println("Error sending message: " + response.getStatusCode());
        }
    }
}

