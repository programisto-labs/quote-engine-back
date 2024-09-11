package com.programisto.devis_rapide.application.generation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programisto.devis_rapide.application.generation.dto.AutocompleteDTO;
import com.programisto.devis_rapide.application.generation.dto.DemandeClientDTO;
import com.programisto.devis_rapide.application.generation.dto.DevisDTO;
import com.programisto.devis_rapide.application.generation.dto.ScenarioChunkDTO;
import com.programisto.devis_rapide.application.generation.service.DevisMapper;
import com.programisto.devis_rapide.domaine.generation.DevisService;
import com.programisto.devis_rapide.domaine.generation.entity.Autocomplete;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.ScenarioChunk;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/devis")
@CrossOrigin(origins = "*")
public class DevisController {
    private final DevisMapper mapper;
    private final DevisService service;

    DevisController(DevisMapper mapper, DevisService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @PostMapping(value = "/genere", consumes = "application/json", produces = "application/json")
    public ResponseEntity<DevisDTO> genereDevis(@RequestBody DemandeClientDTO dto) {
        DemandeClient entity = mapper.mapToDemandeClient(dto);
        Devis devis = service.genereDevis(entity);
        return ResponseEntity.ok(mapper.mapToDevisDTO(devis));
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
