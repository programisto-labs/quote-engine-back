package com.programisto.devis_rapide.application.generation.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModuleApplicatifDTO {
    private String nom;
    private List<ScenarioDTO> scenarios;
}
