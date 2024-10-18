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
@JsonDeserialize(builder = Livraison.LivraisonBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Livraison {
    @NotNull(message = "Le redactionDMFX du livraison ne doit pas être vide")
    @JsonAlias("redactionDMFX")
    @JsonProperty(value = "redactionDMFX", required = true)
    Estimate redactionDMFX;

    @NotNull(message = "Le livraison du livraison ne doit pas être vide")
    @JsonAlias("livraison")
    @JsonProperty(value = "livraison", required = true)
    Estimate livraison;

    @NotNull(message = "Le accompagnement du livraison ne doit pas être vide")
    @JsonAlias("accompagnement")
    @JsonProperty(value = "accompagnement", required = true)
    Estimate accompagnement;

    @NotNull(message = "Le garantie du livraison ne doit pas être vide")
    @JsonAlias("garantie")
    @JsonProperty(value = "garantie", required = true)
    Estimate garantie;

    @JsonAlias("totalCouts")
    @JsonProperty(value = "totalCouts")
    private double totalCouts;

    @JsonAlias("totalJours")
    @JsonProperty(value = "totalJours")
    private double totalJours;

    @Builder
    private Livraison(Estimate redactionDMFX, Estimate livraison, Estimate accompagnement, Estimate garantie) {
        this.redactionDMFX = redactionDMFX;
        this.livraison = livraison;
        this.accompagnement = accompagnement;
        this.garantie = garantie;
        this.totalCouts = calcTotalCouts();
        this.totalJours = calcTotalJours();
    }


    private double calcTotalCouts() {
        return redactionDMFX.getCouts() +
                livraison.getCouts() +
                accompagnement.getCouts() +
                garantie.getCouts();
    }

    private double calcTotalJours() {
        return redactionDMFX.getJours() +
                livraison.getJours() +
                accompagnement.getJours() +
                garantie.getJours();
    }

    public static Livraison.LivraisonBuilder builder() {
        return new Livraison.LivraisonBuilder();
    }

    public static String toJson(Livraison livraison) {
        if (livraison == null) {
            throw new ObjectToJsonConversionException("Livraison", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(livraison);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Livraison", e);
        }
    }

    public static Livraison fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Livraison.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class LivraisonBuilder {
        @JsonProperty(value = "redactionDMFX")
        private Estimate redactionDMFX;

        @JsonProperty(value = "livraison")
        private Estimate livraison;

        @JsonProperty(value = "accompagnement")
        private Estimate accompagnement;

        @JsonProperty(value = "garantie")
        private Estimate garantie;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        LivraisonBuilder() {}

        public LivraisonBuilder redactionDMFX(Estimate redactionDMFX) {
            this.redactionDMFX = redactionDMFX;
            return this;
        }

        public LivraisonBuilder livraison(Estimate livraison) {
            this.livraison = livraison;
            return this;
        }

        public LivraisonBuilder accompagnement(Estimate accompagnement) {
            this.accompagnement = accompagnement;
            return this;
        }

        public LivraisonBuilder garantie(Estimate garantie) {
            this.garantie = garantie;
            return this;
        }

        private void validate(Livraison livraison) {
            Set<ConstraintViolation<Livraison>> violations = validator.validate(livraison);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Livraison> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public Livraison build() {
            Livraison obj = new Livraison(redactionDMFX, livraison, accompagnement, garantie);
            validate(obj);
            return obj;
        }
    }
}
