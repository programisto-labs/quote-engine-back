package com.programisto.devis_rapide.domaine.generation.entity.projet;

import lombok.Getter;

@Getter
public class Projet {
    Conception conception;
    Construction construction;
    Recette recette;
    Livraison livraison;

    public void setConception(Conception conception) {
        this.conception = conception;
    }

    public void setConstruction(Construction construction) {
        this.construction = construction;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public double getTotalCouts() {
        return conception.getTotalCouts() +
                construction.getTotalCouts() +
                recette.getTotalCouts() +
                livraison.getTotalCouts();
    }

    public double getTotalJours() {
        return conception.getTotalJours() +
                construction.getTotalJours() +
                recette.getTotalJours() +
                livraison.getTotalJours();
    }

}