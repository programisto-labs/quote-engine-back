package com.programisto.devis_rapide.application.generation;

import com.programisto.devis_rapide.application.discord_manager.entity.WebhookRawRequest;
import com.programisto.devis_rapide.application.discord_manager.service.DiscordWebhookService;
import com.programisto.devis_rapide.application.entity.HttpResponse;
import com.programisto.devis_rapide.application.generation.dto.*;
import com.programisto.devis_rapide.application.mail_manager.entity.ClientEmailData;
import com.programisto.devis_rapide.application.mail_manager.service.EmailService;
import com.programisto.devis_rapide.domaine.generation.entity.*;
import com.programisto.devis_rapide.domaine.generation.entity.projet.Projet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.programisto.devis_rapide.application.generation.service.DevisMapper;
import com.programisto.devis_rapide.domaine.generation.DevisService;

import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/devis")
public class DevisController {
    private final DevisMapper mapper;
    private final DevisService service;
    private final EmailService emailService;
    private final DiscordWebhookService discordService;

    @Value("${app.discord.webhook}")
    private String discordWebhook;

    DevisController(DevisMapper mapper, DevisService service, EmailService emailService, DiscordWebhookService discordService) {
        this.mapper = mapper;
        this.service = service;
        this.emailService = emailService;
        this.discordService = discordService;
    }

    @PostMapping(value = "/genere", consumes = "application/json", produces = "application/json")
    public ResponseEntity<DevisDTO> genereDevis(@RequestBody DemandeClientDTO dto) {
        DemandeClient entity = mapper.mapToDemandeClient(dto);
        Devis devis = service.genereDevis(entity);
        return ResponseEntity.ok(mapper.mapToDevisDTO(devis));
    }

    @PostMapping(value = "/genere/{chunkSize}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<DevisDTO> genereDevis(@PathVariable("chunkSize") int chunkSize, @RequestBody DemandeClientDTO dto) {
        DemandeClient entity = mapper.mapToDemandeClient(dto);
        Devis devis = service.genereDevis(entity, chunkSize);
        return ResponseEntity.ok(mapper.mapToDevisDTO(devis));
    }

    @PostMapping(value = "/genere/{chunkSize}/{delay}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpResponse> genereDevisWithDelay(@PathVariable("chunkSize") int chunkSize, @PathVariable("delay") int delay, @RequestBody ScheduleDevisGenerationDTO dto) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {
            try {
                System.out.println("Generate davis called at: " + LocalDate.now() + " " + LocalTime.now());

                DemandeClient entity = mapper.mapToDemandeClient(dto.getDemandeClient());
                Devis devis = service.genereDevis(entity, chunkSize);
                Projet projet = Projet.builder().buildFromDevis(devis).build();
                ClientData clientData = dto.getClientData();

                emailService.sendEmail(new ClientEmailData(
                        clientData.getEmail(),
                        clientData.getFullname(),
                        devis,
                        projet
                ));

                WebhookRawRequest wrr = new WebhookRawRequest(discordWebhook, devis, projet, clientData);
                discordService.sendRawMessage(wrr);

                System.out.println("Davis generated successfully at: " + LocalDate.now() + " " + LocalTime.now());
            } catch (Exception e) {
                System.out.println("generateDevisWithDelay: sendEmail: " + e.getMessage());
            }
        }, delay < 5 ? 5 : delay > 86400 ? 86400 : delay, TimeUnit.SECONDS);

        scheduler.shutdown();

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .message("Task scheduled successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping(value = "/scenario/autocomplete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AutocompleteDTO> autocomplete(@RequestBody DemandeClientDTO dto) {
        DemandeClient entity = mapper.mapToDemandeClient(dto);
        Autocomplete suggestions = service.autocomplete(entity);
        return ResponseEntity.ok(mapper.mapToAutocompleteDTO(suggestions));
    }

    @PostMapping(value = "/scenario/autocomplete/inline", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AutocompleteDTO> inlineAutocomplete(@RequestBody ScenarioChunkDTO dto) {
        ScenarioChunk entity = mapper.mapToScenarioChunk(dto);
        Autocomplete suggestions = service.inlineAutocomplete(entity);
        return ResponseEntity.ok(mapper.mapToAutocompleteDTO(suggestions));
    }

    @PostMapping(value = "/demande/new", consumes = "application/json", produces = "application/json")
    public ResponseEntity<DemandeClientDTO> extractDemande(@RequestBody String entity) {
        DemandeClient demandeClient = service.extractDemande(entity);
        return ResponseEntity.ok(mapper.mapToDemandeClientDTO(demandeClient));
    }

}
