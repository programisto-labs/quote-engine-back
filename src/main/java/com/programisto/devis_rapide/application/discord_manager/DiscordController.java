package com.programisto.devis_rapide.application.discord_manager;

import com.programisto.devis_rapide.application.discord_manager.entity.WebhookMessageRequest;
import com.programisto.devis_rapide.application.discord_manager.entity.WebhookRawRequest;
import com.programisto.devis_rapide.application.discord_manager.service.DiscordWebhookService;
import com.programisto.devis_rapide.application.entity.HttpResponse;
import com.programisto.devis_rapide.application.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/devis")
public class DiscordController {

    @Autowired
    private final DiscordWebhookService discordWebhookService;

    public DiscordController(DiscordWebhookService discordWebhookService) {
        this.discordWebhookService = discordWebhookService;
    }

    @PostMapping("/discord/webhook/send")
    public ResponseEntity<HttpResponse> webhookSendMessage(@RequestBody WebhookMessageRequest message) {
        try {
            validateMessageParams(message);
            discordWebhookService.sendMessage(message);
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message("Discord message sent successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
    }

    @PostMapping("/discord/webhook/sendRaw")
    public ResponseEntity<HttpResponse> webhookSendRawMessage(@RequestBody WebhookRawRequest message) {
        try {
            validateRawMessageParams(message);
            discordWebhookService.sendRawMessage(message);
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message("Discord raw message sent successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
    }

    private void validateMessageParams(WebhookMessageRequest message) throws MissingRequestValueException {
        if (!Util.isValid(message.getContent())) {
            throw new MissingRequestValueException("The content to send is required");
        }
        if (!Util.isValid(message.getEmbeds())) {
            throw new MissingRequestValueException("The data to send is required");
        }
        if (!Util.isValid(message.getWebhook())) {
            throw new MissingRequestValueException("The webhook to send to is required");
        }
    }

    private void validateRawMessageParams(WebhookRawRequest message) throws MissingRequestValueException {
        if (message.getDevis() == null) {
            throw new MissingRequestValueException("The devis to send is required");
        }
        if (message.getProjet() == null) {
            throw new MissingRequestValueException("The projet to send is required");
        }
        if (message.getClient() == null) {
            throw new MissingRequestValueException("The client data is required");
        }
        if (!Util.isValid(message.getWebhook())) {
            throw new MissingRequestValueException("The webhook to send to is required");
        }
    }
}


