package com.programisto.devis_rapide.application.generation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScenarioDTO {
    private String nom;
    private String complexite;
    private double duree;
}
