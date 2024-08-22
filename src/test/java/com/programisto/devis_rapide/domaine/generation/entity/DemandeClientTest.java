package com.programisto.devis_rapide.domaine.generation.entity;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

class DemandeClientTest {

        @Test
        void testBuildDemandeClient() {
                // Arrange
                String coreBusiness = "Software Development";
                String concept = "Agile Methodology";
                List<String> useCases = Arrays.asList("User Registration", "Order Processing");

                // Act
                DemandeClient demandeClient = DemandeClient.builder()
                                .coreBusiness(coreBusiness)
                                .concept(concept)
                                .useCases(useCases)
                                .build();

                // Assert
                assertThat(demandeClient)
                                .isNotNull()
                                .hasFieldOrPropertyWithValue("coreBusiness", coreBusiness)
                                .hasFieldOrPropertyWithValue("concept", concept)
                                .hasFieldOrPropertyWithValue("useCases", useCases);
        }

        @Test
        void testToJson() {
                // Arrange
                String coreBusiness = "Software Development";
                String concept = "Agile Methodology";
                List<String> useCases = Arrays.asList("User Registration", "Order Processing");
                DemandeClient demandeClient = DemandeClient.builder()
                                .coreBusiness(coreBusiness)
                                .concept(concept)
                                .useCases(useCases)
                                .build();

                // Act
                String json = DemandeClient.toJson(demandeClient);

                // Assert
                assertThat(json)
                                .isNotNull()
                                .isNotEmpty()
                                .matches("\\{.*\\}")
                                .matches(".*\"core_business\":\"Software Development\".*")
                                .matches(".*\"concept\":\"Agile Methodology\".*")
                                .matches(".*\"useCases\":\\[\"User Registration\",\"Order Processing\"\\].*");
                // Add more assertions to validate the JSON structure if needed
        }

        @Test
        void testFromJson() {
                // Arrange
                String json = "{\"coreBusiness\":\"Software Development\",\"concept\":\"Agile Methodology\",\"useCases\":[\"User Registration\",\"Order Processing\"]}";

                // Act
                DemandeClient demandeClient = DemandeClient.fromJson(json);

                // Assert
                assertThat(demandeClient)
                                .isNotNull()
                                .hasFieldOrPropertyWithValue("coreBusiness", "Software Development")
                                .hasFieldOrPropertyWithValue("concept", "Agile Methodology")
                                .hasFieldOrPropertyWithValue("useCases",
                                                Arrays.asList("User Registration", "Order Processing"));

        }

        @Test
        void testBuilderValidate() {
                // Arrange
                String coreBusiness = "";
                String concept = "";
                List<String> useCases = Arrays.asList("User Registration", "");

                // Act
                DemandeClient.DemandeClientBuilder builder = DemandeClient.builder()
                                .coreBusiness(coreBusiness)
                                .concept(concept)
                                .useCases(useCases);

                // Assert
                assertThatThrownBy(builder::build)
                                .isInstanceOf(ConstraintViolationException.class);
        }
}
