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
@JsonDeserialize(builder = Devis.DevisBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Devis {

    @NotBlank(message = "Le nom du devis ne doit pas être vide")
    @JsonAlias("nom")
    @JsonProperty(value = "nom", required = true)
    private String nom;

    @NotEmpty(message = "La liste des modules est obligatoire")
    @JsonProperty(value = "modules", required = true)
    private List<ModuleApplicatif> modules;

    @JsonProperty(value = "dateOfEstimate")
    private String dateOfEstimate;

    @JsonProperty(value = "modulesQuantity")
    private int modulesQuantity;

    @JsonProperty(value = "totalHours")
    private double totalHours;

    @Builder
    private Devis(String nom, List<ModuleApplicatif> modules, String dateOfEstimate) {
        this.nom = nom;
        this.modules = modules;
        this.dateOfEstimate = dateOfEstimate;
        this.modulesQuantity = modules.size();
        this.totalHours = modules.stream().mapToDouble(ModuleApplicatif::getTotalHours).sum();
    }

    public static Devis.DevisBuilder builder() {
        return new Devis.DevisBuilder();
    }

    public static String toJson(Devis devis) {
        if (devis == null) {
            throw new ObjectToJsonConversionException("Devis", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(devis);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Devis", e);
        }
    }

    public static Devis fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Devis.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    public static Devis merge(Devis devis1, Devis devis2) {
        devis1.modules.addAll(devis2.modules);
        return devis1;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class DevisBuilder {
        @NotBlank(message = "Le nom du devis ne doit pas être vide")
        @JsonProperty("nom")
        private String nom;

        @JsonProperty("modules")
        private List<ModuleApplicatif> modules;

        @JsonProperty("dateOfEstimate")
        private String dateOfEstimate;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        DevisBuilder() {}

        public DevisBuilder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public DevisBuilder modules(List<ModuleApplicatif> modules) {
            this.modules = modules;
            return this;
        }

        public DevisBuilder dateOfEstimate(String dateOfEstimate) {
            this.dateOfEstimate = dateOfEstimate;
            return this;
        }

        private void validate(Devis devis) {
            Set<ConstraintViolation<Devis>> violations = validator.validate(devis);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<Devis> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public Devis build() {
            Devis devis = new Devis(nom, modules, dateOfEstimate);
            validate(devis);
            return devis;
        }
    }

}
