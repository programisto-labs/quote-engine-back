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
@JsonDeserialize(builder = Estimate.EstimateBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Estimate {
    @NotNull(message = "Le couts du estimate ne doit pas être vide")
    @JsonAlias("couts")
    @JsonProperty(value = "couts", required = true)
    double couts;

    @NotNull(message = "Le jours du estimate ne doit pas être vide")
    @JsonAlias("jours")
    @JsonProperty(value = "jours", required = true)
    double jours;

    @Builder
    private Estimate(double couts, double jours) {
        this.couts = couts;
        this.jours = jours;
    }

    public static Estimate.EstimateBuilder builder() {
        return new Estimate.EstimateBuilder();
    }

    public static String toJson(Estimate estimate) {
        if (estimate == null) {
            throw new ObjectToJsonConversionException("Estimate", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(estimate);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Estimate", e);
        }
    }

    public static Estimate fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Estimate.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class EstimateBuilder {
        @JsonProperty("couts")
        private double couts;

        @JsonProperty("jours")
        private double jours;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        EstimateBuilder() {}

        public EstimateBuilder couts(double couts) {
            this.couts = couts;
            return this;
        }

        public EstimateBuilder jours(double jours) {
            this.jours = jours;
            return this;
        }

        private void validate(Estimate estimate) {
            Set<ConstraintViolation<Estimate>> violations = validator.validate(estimate);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Estimate> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public Estimate build() {
            Estimate estimate = new Estimate(couts, jours);
            validate(estimate);
            return estimate;
        }
    }
}
