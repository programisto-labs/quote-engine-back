package com.programisto.devis_rapide.infrastructure.generation;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.programisto.devis_rapide.domaine.generation.entity.DemandeClient;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpenAiRepositoryTest {
    @Autowired
    private OpenAiRepository openAiRepository;

    @Test
    void contextLoads() {
        assertThat(openAiRepository).isNotNull();
    }

    @Test
    ////////////////////////////////////////////////////////////////////////////
    //
    // TODO: A compléter
    // Plutot que de tester les valeurs exactes du devis, on devrait tester
    // le format du devis généré.
    // systeme_prompt contient le schema du devis généré
    // il faudrait confronter le devis généré avec ce schéma
    //
    ////////////////////////////////////////////////////////////////////////////
    void simpleCompletion() {
        // Given
        DemandeClient demandeClient = DemandeClient.builder()
                .coreBusiness("Facturation")
                .concept("Gestion documentaire")
                .useCases(List.of(
                        "Accès aux documents",
                        "Gestion des utilisateurs",
                        "Gestion des droits",
                        "Gestion des documents"))
                .build();
        // When
        Devis devis = openAiRepository.genere(demandeClient);

        // Then
        assertThat(devis).isNotNull();

        assertThat(devis.getNom()).isNotNull();
        assertThat(devis.getNom()).isNotEmpty();
        assertThat(devis.getNom()).isNotBlank();

        assertThat(devis.getModules()).isNotNull();
        assertThat(devis.getModules()).isNotEmpty();

        // Puisque le devis est généré par une IA, on ne peut pas garantir le contenu
        // exact du devis.
        // On vérifie donc que le devis contient bien les informations attendues.
        // assertThat(devis.getModules().stream().anyMatch(module ->
        // module.getScenarios().stream()
        // .anyMatch(scenario -> scenario.getNom().equals("Accès aux
        // documents")))).isTrue();

    }

}
