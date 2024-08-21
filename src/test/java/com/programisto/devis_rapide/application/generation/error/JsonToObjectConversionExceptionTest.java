package com.programisto.devis_rapide.application.generation.error;

import org.junit.jupiter.api.Test;

import com.programisto.devis_rapide.domaine.generation.error.JsonToObjectConversionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonToObjectConversionExceptionTest {

    @Test
    void constructorShouldSetMessageAndCause() {
        // Given
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");

        // When
        JsonToObjectConversionException exception = new JsonToObjectConversionException(message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}