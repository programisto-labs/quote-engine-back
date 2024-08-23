package com.programisto.devis_rapide.domaine.generation.entity;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
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
import lombok.With;

@Getter
@JsonDeserialize(builder = DemandeClient.DemandeClientBuilder.class)
public class DemandeClient {

    @NotBlank(message = "Le champ core_business est obligatoire")
    @JsonProperty(value = "core_business", required = true)
    private String coreBusiness;

    @NotBlank(message = "Le champ concept est obligatoire")
    @JsonProperty(value = "concept", required = true)
    private String concept;

    @NotEmpty(message = "Le champ use_cases est obligatoire")
    @JsonProperty(value = "useCases", required = true)
    @With
    private List<String> useCases;

    @Builder
    private DemandeClient(String coreBusiness, String concept, List<String> useCases) {
        this.coreBusiness = coreBusiness;
        this.concept = concept;
        this.useCases = useCases;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class DemandeClientBuilder {
        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        private String coreBusiness;
        private String concept;
        private List<String> useCases;

        private void validate(DemandeClient devis) {
            Set<ConstraintViolation<DemandeClient>> violations = validator.validate(devis);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<DemandeClient> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public DemandeClient build() {
            DemandeClient devis = new DemandeClient(coreBusiness, concept, useCases);
            validate(devis);
            return devis;
        }

        public DemandeClientBuilder coreBusiness(String coreBusiness) {
            this.coreBusiness = coreBusiness;
            return this;
        }

        public DemandeClientBuilder concept(String concept) {
            this.concept = concept;
            return this;
        }

        public DemandeClientBuilder useCases(List<String> useCases) {
            this.useCases = useCases;
            return this;
        }
    }

    public static String toJson(DemandeClient demandeClient) {
        try {
            return (new ObjectMapper()).writeValueAsString(demandeClient);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("DemandeClient", e);
        }
    }

    public static DemandeClient fromJson(String json) {
        try {
            return (new ObjectMapper()).readValue(json, DemandeClient.class);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("DemandeClient", e);
        }
    }
}
