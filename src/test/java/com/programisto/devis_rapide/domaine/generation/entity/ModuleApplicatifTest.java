package com.programisto.devis_rapide.domaine.generation.entity;

import org.junit.jupiter.api.Test;

import com.programisto.devis_rapide.domaine.generation.error.JsonToObjectConversionException;
import com.programisto.devis_rapide.domaine.generation.error.ObjectToJsonConversionException;

import jakarta.validation.ConstraintViolationException;

import static com.programisto.devis_rapide.domaine.generation.entity.ScenarioTest.SIMPLE_SCENARIO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

class ModuleApplicatifTest {

    private static final Scenario SCENARIO_2 = SIMPLE_SCENARIO.withNom("Scenario2");
    private static final Scenario SCENARIO_1 = SIMPLE_SCENARIO.withNom("Scenario1");
    private static final List<Scenario> SCENARIOS = List.of(SCENARIO_1, SCENARIO_2);

    @Test
    void testModuleApplicatifBuilder() {
        ModuleApplicatif.ModuleApplicatifBuilder builder = ModuleApplicatif.builder();
        assertThat(builder).isNotNull();
    }

    @Test
    void testModuleApplicatifBuilderWithProperValues() {
        String nom = "ModuleApplicatifTest";
        ModuleApplicatif.ModuleApplicatifBuilder builder = ModuleApplicatif.builder().nom(nom).scenarios(SCENARIOS);
        assertThat(builder).isNotNull();
        assertThat(builder.build().getNom()).isEqualTo(nom);
        assertThat(builder.build().getScenarios()).isEqualTo(SCENARIOS);
    }

    @Test
    void testModuleApplicatifBuilderWithValidation() {
        assertThatThrownBy(ModuleApplicatif.builder().nom("")::build).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void testToJson() throws ObjectToJsonConversionException {
        Scenario scenario1 = SCENARIO_1;
        Scenario scenario2 = SCENARIO_2;
        List<Scenario> scenarios = Arrays.asList(scenario1, scenario2);
        ModuleApplicatif moduleApplicatif = ModuleApplicatif.builder()
                .nom("ModuleApplicatifTest")
                .scenarios(scenarios)
                .build();
        String json = ModuleApplicatif.toJson(moduleApplicatif);
        assertThat(json)
                .isNotNull()
                .contains("ModuleApplicatifTest")
                .contains("Scenario1")
                .contains("Scenario2");
    }

    @Test
    void testFromJson() throws JsonToObjectConversionException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"nom\":\"ModuleApplicatifTest\",\"scenarios\":[");
        stringBuilder.append(Scenario.toJson(SCENARIO_1));
        stringBuilder.append(",");
        stringBuilder.append(Scenario.toJson(SCENARIO_2));
        stringBuilder.append("]}");
        String json = stringBuilder.toString();
        ModuleApplicatif moduleApplicatif = ModuleApplicatif.fromJson(json);
        assertThat(moduleApplicatif).isNotNull();
        assertThat(moduleApplicatif.getNom()).isEqualTo("ModuleApplicatifTest");
        assertThat(moduleApplicatif.getScenarios()).hasSize(2);
        assertThat(moduleApplicatif.getScenarios().get(0).getNom()).isEqualTo("Scenario1");
        assertThat(moduleApplicatif.getScenarios().get(1).getNom()).isEqualTo("Scenario2");
    }
}
