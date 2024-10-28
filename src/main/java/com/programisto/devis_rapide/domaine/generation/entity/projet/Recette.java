package com.programisto.devis_rapide.domaine.generation.entity.projet;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.programisto.devis_rapide.domaine.generation.error.JsonToObjectConversionException;
import com.programisto.devis_rapide.domaine.generation.error.ObjectToJsonConversionException;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@JsonDeserialize(builder = Recette.RecetteBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recette {
    @NotNull(message = "Le livraisonRecette du recette ne doit pas être vide")
    @JsonAlias("livraisonRecette")
    @JsonProperty(value = "livraisonRecette", required = true)
    Estimate livraisonRecette;

    @NotNull(message = "Le tests du recette ne doit pas être vide")
    @JsonAlias("tests")
    @JsonProperty(value = "tests", required = true)
    Estimate tests;

    @NotNull(message = "Le corrections du recette ne doit pas être vide")
    @JsonAlias("corrections")
    @JsonProperty(value = "corrections", required = true)
    Estimate corrections;

    @NotNull(message = "Le relecture du recette ne doit pas être vide")
    @JsonAlias("relecture")
    @JsonProperty(value = "relecture", required = true)
    Estimate relecture;

    @NotNull(message = "Le supportTechnique du recette ne doit pas être vide")
    @JsonAlias("supportTechnique")
    @JsonProperty(value = "supportTechnique", required = true)
    Estimate supportTechnique;

    @JsonAlias("totalCouts")
    @JsonProperty(value = "totalCouts")
    private double totalCouts;

    @JsonAlias("totalJours")
    @JsonProperty(value = "totalJours")
    private double totalJours;

    @Builder
    private Recette(Estimate livraisonRecette, Estimate tests, Estimate corrections, Estimate relecture, Estimate supportTechnique) {
        this.livraisonRecette = livraisonRecette;
        this.tests = tests;
        this.corrections = corrections;
        this.relecture = relecture;
        this.supportTechnique = supportTechnique;
        this.totalCouts = calcTotalCouts();
        this.totalJours = calcTotalJours();
    }


    private double calcTotalCouts() {
        return livraisonRecette.getCouts() +
                tests.getCouts() +
                corrections.getCouts() +
                relecture.getCouts() +
                supportTechnique.getCouts();
    }

    private double calcTotalJours() {
        return livraisonRecette.getJours() +
                tests.getJours() +
                corrections.getJours() +
                relecture.getJours() +
                supportTechnique.getJours();
    }

    public static Recette.RecetteBuilder builder() {
        return new Recette.RecetteBuilder();
    }

    public static String toJson(Recette recette) {
        if (recette == null) {
            throw new ObjectToJsonConversionException("Recette", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(recette);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Recette", e);
        }
    }

    public static Recette fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Recette.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class RecetteBuilder {
        @JsonProperty(value = "livraisonRecette")
        private Estimate livraisonRecette;

        @JsonProperty(value = "tests")
        private Estimate tests;

        @JsonProperty(value = "corrections")
        private Estimate corrections;

        @JsonProperty(value = "relecture")
        private Estimate relecture;

        @JsonProperty(value = "supportTechnique")
        private Estimate supportTechnique;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        RecetteBuilder() {}

        public RecetteBuilder livraisonRecette(Estimate livraisonRecette) {
            this.livraisonRecette = livraisonRecette;
            return this;
        }

        public RecetteBuilder tests(Estimate tests) {
            this.tests = tests;
            return this;
        }

        public RecetteBuilder corrections(Estimate corrections) {
            this.corrections = corrections;
            return this;
        }

        public RecetteBuilder relecture(Estimate relecture) {
            this.relecture = relecture;
            return this;
        }

        public RecetteBuilder supportTechnique(Estimate supportTechnique) {
            this.supportTechnique = supportTechnique;
            return this;
        }

        public RecetteBuilder buildFromJours(double jours) {
            livraisonRecette = Estimate.builder()
                    .jours(jours * RecetteCouts.livraisonRecette)
                    .couts(jours * RecetteCouts.livraisonRecette * CoutProfil.leaderTechnique)
                    .build();

            tests = Estimate.builder()
                    .jours(jours * RecetteCouts.tests)
                    .couts(jours * RecetteCouts.tests * CoutProfil.chefDeProjet)
                    .build();

            corrections = Estimate.builder()
                    .jours(jours * RecetteCouts.corrections)
                    .couts(jours * RecetteCouts.corrections * CoutProfil.developpeur)
                    .build();

            relecture = Estimate.builder()
                    .jours(jours * RecetteCouts.relecture)
                    .couts(jours * RecetteCouts.relecture * CoutProfil.leaderTechnique)
                    .build();

            supportTechnique = Estimate.builder()
                    .jours(jours * RecetteCouts.supportTechnique)
                    .couts(jours * RecetteCouts.supportTechnique * CoutProfil.leaderTechnique)
                    .build();

            return this;
        }

        private void validate(Recette recette) {
            Set<ConstraintViolation<Recette>> violations = validator.validate(recette);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Recette> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public Recette build() {
            Recette recette = new Recette(livraisonRecette, tests, corrections, relecture, supportTechnique);
            validate(recette);
            return recette;
        }
    }
}
