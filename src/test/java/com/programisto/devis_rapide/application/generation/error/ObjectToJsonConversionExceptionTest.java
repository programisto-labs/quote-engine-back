package com.programisto.devis_rapide.application.generation.error;

import org.junit.jupiter.api.Test;

import com.programisto.devis_rapide.domaine.generation.error.ObjectToJsonConversionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectToJsonConversionExceptionTest {

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");

        ObjectToJsonConversionException exception = new ObjectToJsonConversionException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}