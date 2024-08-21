package com.programisto.devis_rapide.domaine.generation.entity;

import java.util.List;
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
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = ScenarioChunk.ScenarioChunkBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioChunk {
    @JsonProperty(value = "chunk", required = true)
    @JsonAlias("chunk")
    private String chunk;

    @NotNull(message = "La demande client est obligatoire")
    @JsonProperty(value = "demandeClient", required = true)
    @JsonAlias("demandeClient")
    private DemandeClient demandeClient;

    @Builder
    private ScenarioChunk(String chunk, DemandeClient demandeClient) {
        this.chunk = chunk;
        this.demandeClient = demandeClient;
    }

    public static String toJson(ScenarioChunk scenarioChunk) {
        if (scenarioChunk == null) {
            throw new ObjectToJsonConversionException("ScenarioChunk", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(scenarioChunk);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("ScenarioChunk", e);
        }
    }

    public static ScenarioChunk fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, ScenarioChunk.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ScenarioChunkBuilder {
        @JsonProperty("chunk")
        private String chunk;
        @JsonProperty("demandeClient")
        private DemandeClient demandeClient;
        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        public ScenarioChunkBuilder chunk(String chunk) {
            this.chunk = chunk;
            return this;
        }

        public ScenarioChunkBuilder demandeClient(DemandeClient demandeClient) {
            this.demandeClient = demandeClient;
            return this;
        }

        private void validate(ScenarioChunk scenarioChunk) {
            Set<ConstraintViolation<ScenarioChunk>> violations = validator.validate(scenarioChunk);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }

        public ScenarioChunk build() {
            ScenarioChunk scenarioChunk = new ScenarioChunk(chunk, demandeClient);
            validate(scenarioChunk);
            return scenarioChunk;
        }
    }

}
