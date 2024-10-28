package com.programisto.devis_rapide.application.mail_manager.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.projet.Projet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@JsonDeserialize
@AllArgsConstructor
@NoArgsConstructor
public class ClientEmailData {
    String clientEmail;
    String clientName;
    Devis devis;
    Projet projet;
}
