package com.programisto.devis_rapide.domaine.generation.entity.projet;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonDeserialize(builder = Construction.ConstructionBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Construction {
    @NotNull(message = "Le initialisation du construction ne doit pas être vide")
    @JsonAlias("initialisation")
    @JsonProperty(value = "initialisation", required = true)
    Estimate initialisation;

    @NotNull(message = "Le developementAvecTests du construction ne doit pas être vide")
    @JsonAlias("developementAvecTests")
    @JsonProperty(value = "developementAvecTests", required = true)
    Estimate developementAvecTests;

    @NotNull(message = "Le relecture du construction ne doit pas être vide")
    @JsonAlias("relecture")
    @JsonProperty(value = "relecture", required = true)
    Estimate relecture;

    @NotNull(message = "Le supportTechnique du construction ne doit pas être vide")
    @JsonAlias("supportTechnique")
    @JsonProperty(value = "supportTechnique", required = true)
    Estimate supportTechnique;

    @NotNull(message = "Le supportFonctionnel du construction ne doit pas être vide")
    @JsonAlias("supportFonctionnel")
    @JsonProperty(value = "supportFonctionnel", required = true)
    Estimate supportFonctionnel;

    @NotNull(message = "Le suiviDeProjet du construction ne doit pas être vide")
    @JsonAlias("suiviDeProjet")
    @JsonProperty(value = "suiviDeProjet", required = true)
    Estimate suiviDeProjet;

    @JsonAlias("totalCouts")
    @JsonProperty(value = "totalCouts")
    private double totalCouts;

    @JsonAlias("totalJours")
    @JsonProperty(value = "totalJours")
    private double totalJours;

    @Builder
    private Construction(Estimate initialisation, Estimate developementAvecTests, Estimate relecture, Estimate supportTechnique, Estimate supportFonctionnel, Estimate suiviDeProjet) {
        this.initialisation = initialisation;
        this.developementAvecTests = developementAvecTests;
        this.relecture = relecture;
        this.supportTechnique = supportTechnique;
        this.supportFonctionnel = supportFonctionnel;
        this.suiviDeProjet = suiviDeProjet;
        this.totalCouts = calcTotalCouts();
        this.totalJours = calcTotalJours();
    }

    private double calcTotalCouts() {
        return initialisation.getCouts() +
                developementAvecTests.getCouts() +
                relecture.getCouts() +
                supportTechnique.getCouts() +
                supportFonctionnel.getCouts() +
                suiviDeProjet.getCouts();
    }

    private double calcTotalJours() {
        return initialisation.getJours() +
                developementAvecTests.getJours() +
                relecture.getJours() +
                supportTechnique.getJours() +
                supportFonctionnel.getJours() +
                suiviDeProjet.getJours();
    }

    public static Construction.ConstructionBuilder builder() {
        return new Construction.ConstructionBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ConstructionBuilder {
        @JsonProperty(value = "initialisation")
        private Estimate initialisation;

        @JsonProperty(value = "developementAvecTests")
        private Estimate developementAvecTests;

        @JsonProperty(value = "relecture")
        private Estimate relecture;

        @JsonProperty(value = "supportTechnique")
        private Estimate supportTechnique;

        @JsonProperty(value = "supportFonctionnel")
        private Estimate supportFonctionnel;

        @JsonProperty(value = "suiviDeProjet")
        private Estimate suiviDeProjet;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        ConstructionBuilder() {}

        public ConstructionBuilder initialisation(Estimate initialisation) {
            this.initialisation = initialisation;
            return this;
        }

        public ConstructionBuilder developementAvecTests(Estimate developementAvecTests) {
            this.developementAvecTests = developementAvecTests;
            return this;
        }

        public ConstructionBuilder relecture(Estimate relecture) {
            this.relecture = relecture;
            return this;
        }

        public ConstructionBuilder supportTechnique(Estimate supportTechnique) {
            this.supportTechnique = supportTechnique;
            return this;
        }

        public ConstructionBuilder supportFonctionnel(Estimate supportFonctionnel) {
            this.supportFonctionnel = supportFonctionnel;
            return this;
        }

        public ConstructionBuilder suiviDeProjet(Estimate suiviDeProjet) {
            this.suiviDeProjet = suiviDeProjet;
            return this;
        }

        private void validate(Construction construction) {
            Set<ConstraintViolation<Construction>> violations = validator.validate(construction);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Construction> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public Construction build() {
            Construction construction = new Construction(initialisation, developementAvecTests, relecture, supportTechnique, supportFonctionnel, suiviDeProjet);
            validate(construction);
            return construction;
        }
    }
}
