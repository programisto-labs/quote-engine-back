package com.programisto.devis_rapide.application.generation.dto;

import com.programisto.devis_rapide.domaine.generation.entity.ClientData;
import lombok.Getter;

@Getter
public class ScheduleDevisGenerationDTO {
    ClientData clientData;
    DemandeClientDTO demandeClient;
}
