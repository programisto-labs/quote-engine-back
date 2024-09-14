package com.programisto.devis_rapide.application.discord_manager;

import com.programisto.devis_rapide.application.discord_manager.entity.WebhookMessageRequest;
import com.programisto.devis_rapide.application.discord_manager.service.DiscordWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devis/discord")
public class DiscordController {

    @Autowired
    private DiscordWebhookService discordWebhookService;

    @PostMapping("/webhook/send")
    public void webhookSendMessage(@RequestBody WebhookMessageRequest message) {
        discordWebhookService.sendMessage(message);
    }
}


