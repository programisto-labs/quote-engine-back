package com.programisto.devis_rapide.domaine.generation;

import java.util.List;

import com.programisto.devis_rapide.domaine.generation.entity.Autocomplete;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.ScenarioChunk;

public interface IDevisRepository {
    Devis genere(DemandeClient demandeClient);

    Autocomplete autocomplete(DemandeClient demandeClient);

    Autocomplete inlineAutocomplete(ScenarioChunk entity);
}
