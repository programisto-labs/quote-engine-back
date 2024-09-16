package com.programisto.devis_rapide.application.discord_manager.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WebhookMessageRequest {
    String content;
    String embeds;
}
