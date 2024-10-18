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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = ModuleApplicatif.ModuleApplicatifBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModuleApplicatif {

    @NotBlank(message = "Le nom du module applicatif ne doit pas être vide")
    @JsonAlias("nom")
    @JsonProperty(value = "nom", required = true)
    private String nom;

    @NotEmpty(message = "Le champ useCases est obligatoire")
    @JsonAlias("scenarios")
    @JsonProperty(value = "scenarios", required = true, access = JsonProperty.Access.READ_ONLY)
    private List<Scenario> scenarios;

    @JsonAlias("scenariosQuantity")
    @JsonProperty(value = "scenariosQuantity")
    private int scenariosQuantity;

    @JsonAlias("totalHours")
    @JsonProperty(value = "totalHours")
    private double totalHours;

    @Builder
    private ModuleApplicatif(String nom, List<Scenario> scenarios) {
        this.nom = nom;
        this.scenarios = scenarios;
        this.scenariosQuantity = scenarios.size();
        this.totalHours = scenarios.stream().mapToDouble(Scenario::getDuree).sum();
    }

    public static String toJson(ModuleApplicatif moduleApplicatif) {
        if (moduleApplicatif == null) {
            throw new ObjectToJsonConversionException("ModuleApplicatif", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(moduleApplicatif);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("ModuleApplicatif", e);
        }
    }

    public static ModuleApplicatif fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, ModuleApplicatif.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ModuleApplicatifBuilder {
        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();
        @JsonProperty("nom")
        private String nom;

        @JsonProperty("scenarios")
        private List<Scenario> scenarios;

        public ModuleApplicatifBuilder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public ModuleApplicatifBuilder scenarios(List<Scenario> scenarios) {
            this.scenarios = scenarios;
            return this;
        }

        public ModuleApplicatif build() {
            ModuleApplicatif moduleApplicatif = new ModuleApplicatif(nom, scenarios);
            validate(moduleApplicatif);
            return moduleApplicatif;
        }

        public void validate(ModuleApplicatif moduleApplicatif) {
            Set<ConstraintViolation<ModuleApplicatif>> violations = validator.validate(moduleApplicatif);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<ModuleApplicatif> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }

        }
    }

}
