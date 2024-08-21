package com.programisto.devis_rapide.domaine.generation.entity;

import org.junit.jupiter.api.Test;

import com.programisto.devis_rapide.domaine.generation.error.JsonToObjectConversionException;
import com.programisto.devis_rapide.domaine.generation.error.ObjectToJsonConversionException;

import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

class DevisTest {

    private static Devis getSampleDevis() {
        Scenario sampleScenario = Scenario.builder().nom("foo").complexite("medium").duree(1).build();
        ModuleApplicatif sampleModule = ModuleApplicatif.builder().nom("Module 1").scenarios(List.of(sampleScenario))
                .build();
        return Devis.builder().nom("Sample Devis").modules(List.of(sampleModule)).build();
    }

    private static final String SAMPLE_DEVIS_JSON = "{\"modules\":[{\"name\":\"Module 1\",\"useCases\":[{\"name\":\"foo\",\"complexity\":\"medium\",\"durationInDays\":1.0}]}],\"name\":\"Sample Devis\"}";

    @Test
    void toJson() {
        // When
        String json = Devis.toJson(getSampleDevis());

        // Then
        assertThat(json)
                .isNotNull()
                .isNotEmpty()
                .matches("\\{.*\\}")
                .matches(".*\"name\":\"Sample Devis\".*")
                .matches(".*\"modules\":\\[\\{.*\\}\\].*");
    }

    @Test
    void fromJson() {
        // Given

        // When
        Devis devis = Devis.fromJson(SAMPLE_DEVIS_JSON);

        // Then
        assertThat(devis).isNotNull();
        assertThat(devis.getNom()).isEqualTo("Sample Devis");
        assertThat(devis.getModules()).hasSize(1);
        assertThat(devis.getModules().get(0).getNom()).isEqualTo("Module 1");
    }

    @Test
    void toJson_ThrowsObjectToJsonConversionException() {
        assertThatThrownBy(() -> Devis.toJson(null))
                .isInstanceOf(ObjectToJsonConversionException.class)
                .hasMessageContaining("Devis");
    }

    @Test
    void buildDevisWithEmptyNom_ThrowsValidationException() {
        // Given
        String emptyNom = "";

        // When
        assertThatThrownBy(() -> Devis.builder().nom(emptyNom).build())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Le nom du devis ne doit pas être vide");
    }

    @Test
    void buildDevisWithEmptyModules_ThrowsValidationException() {
        // Given
        List<ModuleApplicatif> emptyModules = Collections.emptyList();

        // When
        assertThatThrownBy(() -> Devis.builder().nom("Sample Devis").modules(emptyModules).build())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("La liste des modules est obligatoire");
    }

    @Test
    void toJsonWithNullDevis_ThrowsObjectToJsonConversionException() {
        // When
        assertThatThrownBy(() -> Devis.toJson(null))
                .isInstanceOf(ObjectToJsonConversionException.class)
                .hasMessageContaining("Devis");
    }

    @Test
    void fromJsonWithInvalidJson_ThrowsJsonToObjectConversionException() {
        // Given
        String invalidJson = "{\"invalid\":\"json\"}";

        // When
        assertThatThrownBy(() -> Devis.fromJson(invalidJson))
                .isInstanceOf(JsonToObjectConversionException.class)
                .hasMessageContaining(invalidJson);
    }
}