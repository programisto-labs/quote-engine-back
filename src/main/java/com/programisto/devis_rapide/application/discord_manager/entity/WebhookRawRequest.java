package com.programisto.devis_rapide.application.discord_manager.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.programisto.devis_rapide.domaine.generation.entity.ClientData;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.projet.Projet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize
public class WebhookRawRequest {
    String webhook;
    Devis devis;
    Projet projet;
    ClientData client;
}
