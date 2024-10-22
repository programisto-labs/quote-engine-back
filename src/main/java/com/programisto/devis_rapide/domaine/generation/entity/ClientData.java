package com.programisto.devis_rapide.domaine.generation.entity;

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
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import java.util.Set;

@Getter
@JsonDeserialize(builder = ClientData.ClientDataBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientData {
    @NotBlank(message = "Le nom du client ne doit pas être vide")
    @JsonAlias("fullname")
    @JsonProperty(value = "fullname", required = true)
    private String fullname;

    @NotBlank(message = "Le company du client ne doit pas être vide")
    @JsonAlias("company")
    @JsonProperty(value = "company", required = true)
    private String company;

    @NotBlank(message = "Le email du client ne doit pas être vide")
    @JsonAlias("email")
    @JsonProperty(value = "email", required = true)
    private String email;

    @NotBlank(message = "Le tele du client ne doit pas être vide")
    @JsonAlias("tele")
    @JsonProperty(value = "tele", required = true)
    private String tele;

    @JsonAlias("coreBusiness")
    @JsonProperty(value = "coreBusiness")
    private String coreBusiness;

    @JsonAlias("concept")
    @JsonProperty(value = "concept", required = true)
    private String concept;

    @Builder
    private ClientData(String fullname, String company, String email, String tele, String coreBusiness, String concept ) {
        this.fullname = fullname;
        this.company = company;
        this.email = email;
        this.tele = tele;
        this.coreBusiness = coreBusiness;
        this.concept = concept;
    }

    public static ClientData.ClientDataBuilder builder() {
        return new ClientData.ClientDataBuilder();
    }

    public static String toJson(ClientData client) {
        if (client == null) {
            throw new ObjectToJsonConversionException("ClientData", new NullPointerException());
        }
        try {
            return (new ObjectMapper()).writeValueAsString(client);
        } catch (JsonProcessingException e) {
            throw new ObjectToJsonConversionException("ClientData", e);
        }
    }

    public static ClientData fromJson(String json) {
        if (json == null) {
            throw new JsonToObjectConversionException(json, new NullPointerException());
        }
        try {
            return (new ObjectMapper()).readValue(json, ClientData.class);
        } catch (JsonProcessingException e) {
            throw new JsonToObjectConversionException(json, e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ClientDataBuilder {
        @NotBlank(message = "Le nom du client ne doit pas être vide")
        @JsonProperty(value = "fullname", required = true)
        private String fullname;

        @NotBlank(message = "Le company du client ne doit pas être vide")
        @JsonProperty(value = "company", required = true)
        private String company;

        @NotBlank(message = "Le email du client ne doit pas être vide")
        @JsonProperty(value = "email", required = true)
        private String email;

        @NotBlank(message = "Le tele du client ne doit pas être vide")
        @JsonProperty(value = "tele", required = true)
        private String tele;

        @JsonProperty(value = "coreBusiness")
        private String coreBusiness;

        @JsonProperty(value = "concept", required = true)
        private String concept;

        private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        private static final Validator validator = factory.getValidator();

        ClientDataBuilder() {}

        public ClientData.ClientDataBuilder fullname(String fullname) {
            this.fullname = fullname;
            return this;
        }

        public ClientData.ClientDataBuilder company(String company) {
            this.company = company;
            return this;
        }

        public ClientData.ClientDataBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ClientData.ClientDataBuilder tele(String tele) {
            this.tele = tele;
            return this;
        }

        public ClientData.ClientDataBuilder coreBusiness(String coreBusiness) {
            this.coreBusiness = coreBusiness;
            return this;
        }

        public ClientData.ClientDataBuilder concept(String concept) {
            this.concept = concept;
            return this;
        }

        private void validate(ClientData client) {
            Set<ConstraintViolation<ClientData>> violations = validator.validate(client);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<ClientData> violation : violations) {
                    sb.append(violation.getMessage()).append("\n");
                }
                throw new ConstraintViolationException(sb.toString(), violations);
            }
        }

        public ClientData build() {
            ClientData client = new ClientData(fullname, company, email, tele, coreBusiness, concept);
            validate(client);
            return client;
        }
    }

}
