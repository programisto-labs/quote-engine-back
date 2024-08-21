package com.programisto.devis_rapide.application.generation.service;

import org.springframework.stereotype.Service;

import com.programisto.devis_rapide.application.generation.dto.AutocompleteDTO;
import com.programisto.devis_rapide.application.generation.dto.DemandeClientDTO;
import com.programisto.devis_rapide.application.generation.dto.DevisDTO;
import com.programisto.devis_rapide.application.generation.dto.ModuleApplicatifDTO;
import com.programisto.devis_rapide.application.generation.dto.ScenarioChunkDTO;
import com.programisto.devis_rapide.application.generation.dto.ScenarioDTO;
import com.programisto.devis_rapide.domaine.generation.entity.Autocomplete;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.ModuleApplicatif;
import com.programisto.devis_rapide.domaine.generation.entity.Scenario;
import com.programisto.devis_rapide.domaine.generation.entity.ScenarioChunk;

@Service
public class DevisMapper {
    public DemandeClient mapToDemandeClient(DemandeClientDTO demandeClient) {
        return DemandeClient.builder()
                .coreBusiness(demandeClient.getCoreBusiness())
                .concept(demandeClient.getConcept())
                .useCases(demandeClient.getUseCases())
                .build();
    }

    public DevisDTO mapToDevisDTO(Devis devis) {
        return DevisDTO.builder()
                .nom(devis.getNom())
                .modules(devis.getModules().stream().map(this::mapToModuleApplicatifDTO).toList())
                .build();
    }

    public ModuleApplicatifDTO mapToModuleApplicatifDTO(ModuleApplicatif module) {
        return ModuleApplicatifDTO.builder()
                .nom(module.getNom())
                .scenarios(module.getScenarios().stream().map(this::mapToScenarioDTO).toList())
                .build();
    }

    public ScenarioDTO mapToScenarioDTO(Scenario scenario) {
        return ScenarioDTO.builder()
                .nom(scenario.getNom())
                .complexite(scenario.getComplexite())
                .duree(scenario.getDuree())
                .build();
    }

    public AutocompleteDTO mapToAutocompleteDTO(Autocomplete completions) {
        return AutocompleteDTO.builder()
                .suggestions(completions.getSuggestions())
                .build();
    }

    public ScenarioChunk mapToScenarioChunk(ScenarioChunkDTO dto) {
        return ScenarioChunk.builder()
                .demandeClient(mapToDemandeClient(dto.getDemandeClient()))
                .chunk(dto.getChunk())
                .build();
    }
}
