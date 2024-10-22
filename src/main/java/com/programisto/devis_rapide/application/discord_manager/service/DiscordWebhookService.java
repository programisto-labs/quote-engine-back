package com.programisto.devis_rapide.application.discord_manager.service;

import com.programisto.devis_rapide.application.discord_manager.entity.WebhookMessageRequest;
import com.programisto.devis_rapide.application.discord_manager.entity.WebhookRawRequest;

public interface DiscordWebhookService {
    public void sendMessage(WebhookMessageRequest content);

    public void sendRawMessage(WebhookRawRequest content);
}
