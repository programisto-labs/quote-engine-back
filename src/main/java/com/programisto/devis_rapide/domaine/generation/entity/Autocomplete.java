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
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = Autocomplete.AutocompleteBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Autocomplete {
    @NotEmpty(message = "La liste des suggestions est obligatoire")
    @JsonProperty(value = "suggestions", required = true)
    @JsonAlias("suggestions")
    private List<String> suggestions;

    @Builder
    private Autocomplete(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public static String toJson(Autocomplete autocomplete) {
        if (autocomplete == null) {
            throw new ObjectToJsonConversionException("Autocomplete", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(autocomplete);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("Autocomplete", e);
        }
    }

    public static Autocomplete fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, Autocomplete.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AutocompleteBuilder {
        @JsonProperty("suggestions")
        private List<String> suggestions;
        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        public AutocompleteBuilder suggestions(List<String> suggestions) {
            this.suggestions = suggestions;
            return this;
        }

        private void validate(Autocomplete autocomplete) {
            Set<ConstraintViolation<Autocomplete>> violations = validator.validate(autocomplete);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }

        public Autocomplete build() {
            Autocomplete autocomplete = new Autocomplete(suggestions);
            validate(autocomplete);
            return autocomplete;
        }
    }

}
