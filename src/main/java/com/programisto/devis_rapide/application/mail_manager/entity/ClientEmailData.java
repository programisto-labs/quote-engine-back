package com.programisto.devis_rapide.application.mail_manager.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.projet.Projet;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@JsonDeserialize
public class ClientEmailData {
    String clientEmail;
    String clientName;
    String subject;
    Devis devis;
    Projet projet;
}
