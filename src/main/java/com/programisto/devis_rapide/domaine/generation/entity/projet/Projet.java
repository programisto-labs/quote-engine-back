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
@JsonDeserialize(builder = Projet.ProjetBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Projet {
    @NotNull(message = "Le conception du projet ne doit pas être vide")
    @JsonAlias("conception")
    @JsonProperty(value = "conception", required = true)
    private Conception conception;

    @NotNull(message = "Le construction du projet ne doit pas être vide")
    @JsonAlias("construction")
    @JsonProperty(value = "construction", required = true)
    private Construction construction;

    @NotNull(message = "Le recette du projet ne doit pas être vide")
    @JsonAlias("recette")
    @JsonProperty(value = "recette", required = true)
    private Recette recette;

    @NotNull(message = "Le livraison du projet ne doit pas être vide")
    @JsonAlias("livraison")
    @JsonProperty(value = "livraison", required = true)
    private Livraison livraison;

    @JsonAlias("totalCouts")
    @JsonProperty(value = "totalCouts")
    private double totalCouts;

    @JsonAlias("totalJours")
    @JsonProperty(value = "totalJours")
    private double totalJours;

    @Builder
    private Projet(Conception conception, Construction construction, Recette recette, Livraison livraison) {
        this.conception = conception;
        this.construction = construction;
        this.recette = recette;
        this.livraison = livraison;
        this.totalCouts = calcTotalCouts();
        this.totalJours = calcTotalJours();
    }

    private double calcTotalCouts() {
        return conception.getTotalCouts() +
                construction.getTotalCouts() +
                recette.getTotalCouts() +
                livraison.getTotalCouts();
    }

    private double calcTotalJours() {
        return conception.getTotalJours() +
                construction.getTotalJours() +
                recette.getTotalJours() +
                livraison.getTotalJours();
    }

    public static Projet.ProjetBuilder builder() {
        return new Projet.ProjetBuilder();
    }

    public static String toJson(Projet projet) {
        if (projet == null) {
            throw new ObjectToJsonConversionException("Projet", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(projet);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Projet", e);
        }
    }

    public static Projet fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Projet.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ProjetBuilder {
        @JsonProperty(value = "conception")
        private Conception conception;

        @JsonProperty(value = "construction")
        private Construction construction;

        @JsonProperty(value = "recette")
        private Recette recette;

        @JsonProperty(value = "livraison")
        private Livraison livraison;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        ProjetBuilder() {}

        public ProjetBuilder conception(Conception conception) {
            this.conception = conception;
            return this;
        }

        public ProjetBuilder construction(Construction construction) {
            this.construction = construction;
            return this;
        }

        public ProjetBuilder recette(Recette recette) {
            this.recette = recette;
            return this;
        }

        public ProjetBuilder livraison(Livraison livraison) {
            this.livraison = livraison;
            return this;
        }

        private void validate(Projet projet) {
            Set<ConstraintViolation<Projet>> violations = validator.validate(projet);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Projet> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public Projet build() {
            Projet projet = new Projet(conception, construction, recette, livraison);
            validate(projet);
            return projet;
        }
    }
}