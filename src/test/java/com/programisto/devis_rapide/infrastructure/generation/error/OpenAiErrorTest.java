package com.programisto.devis_rapide.infrastructure.generation.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenAiErrorTest {

    @Test
    void testConstructor() {
        String errorMessage = "Test error message";
        OpenAiError error = new OpenAiError(errorMessage);

        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    void testInheritance() {
        String errorMessage = "Test error message";
        OpenAiError error = new OpenAiError(errorMessage);

        assertThat(error).isInstanceOf(RuntimeException.class);
    }
}