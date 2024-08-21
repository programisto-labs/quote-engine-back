
package com.programisto.devis_rapide.domaine.generation;

import org.springframework.stereotype.Service;

import com.programisto.devis_rapide.domaine.generation.entity.Autocomplete;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.ScenarioChunk;

@Service
public class DevisService {
    private IDevisRepository repository;

    DevisService(IDevisRepository repository) {
        this.repository = repository;
    }

    public Devis genereDevis(DemandeClient demandeClient) {
        return repository.genere(demandeClient);
    }

    public Autocomplete autocomplete(DemandeClient demandeClient) {
        return repository.autocomplete(demandeClient);
    }

    public Autocomplete inlineAutocomplete(ScenarioChunk scenarioChunk) {
        return repository.inlineAutocomplete(scenarioChunk);
    }
}