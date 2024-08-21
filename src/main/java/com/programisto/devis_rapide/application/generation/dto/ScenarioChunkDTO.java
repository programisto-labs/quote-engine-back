package com.programisto.devis_rapide.application.generation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioChunkDTO {
    private DemandeClientDTO demandeClient;
    private String chunk;
}
