package com.programisto.devis_rapide.domaine.generation;


import com.programisto.devis_rapide.domaine.generation.entity.Autocomplete;
import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.ScenarioChunk;

public interface IDevisRepository {
    Devis genere(DemandeClient demandeClient);

    Devis genere(DemandeClient demandeClient, int chunkSize);

    Autocomplete autocomplete(DemandeClient demandeClient);

    Autocomplete inlineAutocomplete(ScenarioChunk entity);

    DemandeClient extractDemande(String entity);
}
