package com.programisto.devis_rapide.domaine.generation.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.programisto.devis_rapide.domaine.generation.error.JsonToObjectConversionException;
import com.programisto.devis_rapide.domaine.generation.error.ObjectToJsonConversionException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@Getter
@JsonDeserialize(builder = Scenario.ScenarioBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scenario {

    @With
    @NotBlank(message = "Le nom d'un scénario ne doit pas être vide.")
    @JsonAlias("nom")
    @JsonProperty(value = "name", required = true)
    private String nom;

    @With
    @Pattern(regexp = "^(low|medium|high)$", message = "La complexité d'un scénario doit être low, medium ou high.")
    @NotBlank(message = "La complexité d'un scénario doit être low, medium ou high.")
    @JsonAlias("complexite")
    @JsonProperty(value = "complexity", required = true)
    private String complexite;

    @With
    @Min(value = 1, message = "La durée d'un scénario doit être comprise entre 1 et 10 jours.")
    @Max(value = 10, message = "La durée d'un scénario doit être comprise entre 1 et 10 jours.")
    @JsonAlias("duree")
    @JsonProperty(value = "durationInDays", required = true)
    private double duree;

    @Builder
    private Scenario(String nom, String complexite, double duree) {
        this.nom = nom;
        this.complexite = complexite;
        this.duree = duree;
    }

    public static String toJson(Scenario scenario) {
        if (scenario == null) {
            throw new ObjectToJsonConversionException("Scenario", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(scenario);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Scenario", e);
        }
    }

    public static Scenario fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Scenario.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ScenarioBuilder {
        @JsonProperty("name")
        private String nom;

        @JsonProperty("complexity")
        private String complexite;

        @JsonProperty("durationInDays")
        private double duree;

        public ScenarioBuilder nom(String nom) {
            this.nom = nom;
            return this;
        }

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        public ScenarioBuilder complexite(String complexite) {
            if (complexite == null) {
                throw new IllegalArgumentException("La complexité d'un scénario ne peut pas être nulle.");
            }
            this.complexite = complexite;
            return this;
        }

        public ScenarioBuilder duree(double duree) {
            this.duree = duree;
            return this;
        }

        private void validate(Scenario scenario) {
            Set<ConstraintViolation<Scenario>> violations = validator.validate(scenario);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Scenario> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new IllegalArgumentException(sb.toString());
            }
        }

        public Scenario build() {
            Scenario scenario = new Scenario(nom, complexite, duree);
            validate(scenario);
            return scenario;
        }
    }

}
