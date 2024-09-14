package com.programisto.devis_rapide.application.discord_manager.service;

import com.programisto.devis_rapide.application.discord_manager.entity.WebhookMessageRequest;

public interface DiscordWebhookService {
    public void sendMessage(WebhookMessageRequest content);
}
