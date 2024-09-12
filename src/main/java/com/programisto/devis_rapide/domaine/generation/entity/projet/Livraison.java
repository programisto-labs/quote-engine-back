package com.programisto.devis_rapide.domaine.generation.entity.projet;


import lombok.Getter;

@Getter
public class Livraison {
    Estimate redactionDMFX;
    Estimate livraison;
    Estimate accompagnement;
    Estimate garantie;

    public double getTotalCouts() {
        return redactionDMFX.couts +
                livraison.couts +
                accompagnement.couts +
                garantie.couts;
    }

    public double getTotalJours() {
        return redactionDMFX.jours +
                livraison.jours +
                accompagnement.jours +
                garantie.jours;
    }
}
