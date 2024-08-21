package com.programisto.devis_rapide.domaine.generation.entity;

import org.junit.jupiter.api.Test;

import com.programisto.devis_rapide.domaine.generation.error.JsonToObjectConversionException;
import com.programisto.devis_rapide.domaine.generation.error.ObjectToJsonConversionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScenarioTest {

    public static final Scenario SIMPLE_SCENARIO = Scenario.builder()
            .nom("Accès aux documents")
            .complexite("medium")
            .duree(5.0)
            .build();
    public static final String SIMPLE_JSON_SCENARIO = "{\"name\":\"Accès aux documents\",\"complexity\":\"medium\",\"durationInDays\":5.0}";
    private static final String INVALID_COMPLEXITY_JSON_SCENARIO = "{\"name\":\"Accès aux documents\",\"complexity\":\"invalid\",\"durationInDays\":5.0}";

    @Test
    void toJson() {
        // When
        String json = Scenario.toJson(SIMPLE_SCENARIO);
        // Then
        assertThat(json)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(SIMPLE_JSON_SCENARIO);
    }

    @Test
    void fromJson() {
        // When
        Scenario scenario = Scenario.fromJson(SIMPLE_JSON_SCENARIO);
        // Then
        assertThat(scenario).isNotNull();
        assertThat(scenario.getNom()).isEqualTo("Accès aux documents");
        assertThat(scenario.getComplexite()).isEqualTo("medium");
        assertThat(scenario.getDuree()).isEqualTo(5.0);
    }

    @Test
    void toJson_withNullScenario_shouldThrowException() {
        // When/Then
        assertThatThrownBy(() -> Scenario.toJson(null))
                .isInstanceOf(ObjectToJsonConversionException.class)
                .hasMessageContaining("Scenario")
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void toJson_withValidScenario_shouldReturnJsonString() {
        // Given
        Scenario scenario = Scenario.builder()
                .nom("Test scenario")
                .complexite("high")
                .duree(7.5)
                .build();

        // When
        String json = Scenario.toJson(scenario);

        // Then
        assertThat(json)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("{\"name\":\"Test scenario\",\"complexity\":\"high\",\"durationInDays\":7.5}");
    }

    @Test
    void fromJson_withInvalidComplexite_shouldThrowException() {
        // When/Then
        assertThatThrownBy(() -> Scenario.fromJson(INVALID_COMPLEXITY_JSON_SCENARIO))
                .isInstanceOf(JsonToObjectConversionException.class)
                .hasMessageContaining(INVALID_COMPLEXITY_JSON_SCENARIO);
    }
}