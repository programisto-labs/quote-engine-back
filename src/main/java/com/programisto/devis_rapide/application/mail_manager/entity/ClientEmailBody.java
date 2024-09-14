package com.programisto.devis_rapide.application.mail_manager.entity;

import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.projet.Projet;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ClientEmailBody {
    String to;
    String subject;
    Devis devis;
    Projet projet;
}
