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
@JsonDeserialize(builder = Conception.ConceptionBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conception {
    @NotNull(message = "Le etude du conception ne doit pas être vide")
    @JsonAlias("etude")
    @JsonProperty(value = "etude", required = true)
    Estimate etude;

    @NotNull(message = "Le conceptionFonctionnelle du conception ne doit pas être vide")
    @JsonAlias("conceptionFonctionnelle")
    @JsonProperty(value = "conceptionFonctionnelle", required = true)
    Estimate conceptionFonctionnelle;

    @NotNull(message = "Le conceptionTechnique du conception ne doit pas être vide")
    @JsonAlias("conceptionTechnique")
    @JsonProperty(value = "conceptionTechnique", required = true)
    Estimate conceptionTechnique;

    @NotNull(message = "Le conceptionCahierDeTests du conception ne doit pas être vide")
    @JsonAlias("conceptionCahierDeTests")
    @JsonProperty(value = "conceptionCahierDeTests", required = true)
    Estimate conceptionCahierDeTests;

    @JsonAlias("totalCouts")
    @JsonProperty(value = "totalCouts")
    private double totalCouts;

    @JsonAlias("totalJours")
    @JsonProperty(value = "totalJours")
    private double totalJours;

    @Builder
    private Conception(Estimate etude, Estimate conceptionFonctionnelle, Estimate conceptionTechnique, Estimate conceptionCahierDeTests) {
        this.etude = etude;
        this.conceptionFonctionnelle = conceptionFonctionnelle;
        this.conceptionTechnique = conceptionTechnique;
        this.conceptionCahierDeTests = conceptionCahierDeTests;
        this.totalCouts = calcTotalCouts();
        this.totalJours = calcTotalJours();
    }

    private double calcTotalCouts() {
        return etude.getCouts() +
                conceptionFonctionnelle.getCouts() +
                conceptionTechnique.getCouts() +
                conceptionCahierDeTests.getCouts();
    }

    private double calcTotalJours() {
        return etude.getJours() +
                conceptionFonctionnelle.getJours() +
                conceptionTechnique.getJours() +
                conceptionCahierDeTests.getJours();
    }

    public static Conception.ConceptionBuilder builder() {
        return new Conception.ConceptionBuilder();
    }

    public static String toJson(Conception conception) {
        if (conception == null) {
            throw new ObjectToJsonConversionException("Conception", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(conception);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Conception", e);
        }
    }

    public static Conception fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Conception.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ConceptionBuilder {
        @JsonProperty(value = "etude")
        private Estimate etude;

        @JsonProperty(value = "conceptionFonctionnelle")
        private Estimate conceptionFonctionnelle;

        @JsonProperty(value = "conceptionTechnique")
        private Estimate conceptionTechnique;

        @JsonProperty(value = "conceptionCahierDeTests")
        private Estimate conceptionCahierDeTests;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        ConceptionBuilder() {}

        public ConceptionBuilder etude(Estimate etude) {
            this.etude = etude;
            return this;
        }

        public ConceptionBuilder conceptionFonctionnelle(Estimate conceptionFonctionnelle) {
            this.conceptionFonctionnelle = conceptionFonctionnelle;
            return this;
        }

        public ConceptionBuilder conceptionTechnique(Estimate conceptionTechnique) {
            this.conceptionTechnique = conceptionTechnique;
            return this;
        }

        public ConceptionBuilder conceptionCahierDeTests(Estimate conceptionCahierDeTests) {
            this.conceptionCahierDeTests = conceptionCahierDeTests;
            return this;
        }

        private void validate(Conception conception) {
            Set<ConstraintViolation<Conception>> violations = validator.validate(conception);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Conception> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public Conception build() {
            Conception conception = new Conception(etude, conceptionFonctionnelle, conceptionTechnique, conceptionCahierDeTests);
            validate(conception);
            return conception;
        }
    }
}
