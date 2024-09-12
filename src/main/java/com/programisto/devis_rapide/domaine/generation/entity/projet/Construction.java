package com.programisto.devis_rapide.domaine.generation.entity.projet;


import lombok.Getter;

@Getter
public class Construction {
    Estimate initialisation;
    Estimate developementAvecTests;
    Estimate relecture;
    Estimate supportTechnique;
    Estimate supportFonctionnel;
    Estimate suiviDeProjet;

    public double getTotalCouts() {
        return initialisation.couts +
                developementAvecTests.couts +
                relecture.couts +
                supportTechnique.couts +
                supportFonctionnel.couts +
                suiviDeProjet.couts;
    }

    public double getTotalJours() {
        return initialisation.jours +
                developementAvecTests.jours +
                relecture.jours +
                supportTechnique.jours +
                supportFonctionnel.jours +
                suiviDeProjet.jours;
    }
}
