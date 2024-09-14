/*package com.programisto.devis_rapide.application.generation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.programisto.devis_rapide.application.generation.dto.DemandeClientDTO;
import com.programisto.devis_rapide.application.generation.dto.DevisDTO;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@SpringBootTest
class DevisControllerTest {

    @Autowired
    private DevisController devisController;

    @Test
    void contextLoads() {
        assertThat(devisController).isNotNull();
    }

    @Test
    void genereDevis_ReturnsDevisDTO() {
        // Arrange
        DemandeClientDTO demandeClientDTO = DemandeClientDTO.builder()
                .coreBusiness("Software Development")
                .concept("Agile Methodology")
                .useCases(List.of("User Registration", "Order Processing"))
                .build();
        // Set up the demandeClientDTO object with the necessary data for the test

        // Act
        ResponseEntity<DevisDTO> response = devisController.genereDevis(demandeClientDTO);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DevisDTO body = response.getBody();
        assertThat(body)
                .isNotNull();
        // Add additional assertions to verify the correctness of the returned DevisDTO
    }

}*/
