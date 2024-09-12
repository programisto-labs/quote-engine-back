package com.programisto.devis_rapide.domaine.generation.entity.projet;

import lombok.Getter;

@Getter
public class Conception {
    Estimate etude;
    Estimate conceptionFonctionnelle;
    Estimate conceptionTechnique;
    Estimate conceptionCahierDeTests;

    public double getTotalCouts() {
        return etude.couts +
                conceptionFonctionnelle.couts +
                conceptionTechnique.couts +
                conceptionCahierDeTests.couts;
    }

    public double getTotalJours() {
        return etude.jours +
                conceptionFonctionnelle.jours +
                conceptionTechnique.jours +
                conceptionCahierDeTests.jours;
    }
}
