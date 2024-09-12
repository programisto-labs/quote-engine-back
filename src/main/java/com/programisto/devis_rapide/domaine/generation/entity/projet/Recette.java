package com.programisto.devis_rapide.domaine.generation.entity.projet;


import lombok.Getter;

@Getter
public class Recette {
    Estimate livraisonRecette;
    Estimate tests;
    Estimate corrections;
    Estimate relecture;
    Estimate supportTechnique;

    public double getTotalCouts() {
        return livraisonRecette.couts +
                tests.couts +
                corrections.couts +
                relecture.couts +
                supportTechnique.couts;
    }

    public double getTotalJours() {
        return livraisonRecette.jours +
                tests.jours +
                corrections.jours +
                relecture.jours +
                supportTechnique.jours;
    }
}
