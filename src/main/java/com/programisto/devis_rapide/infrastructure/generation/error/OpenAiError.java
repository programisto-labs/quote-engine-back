package com.programisto.devis_rapide.infrastructure.generation.error;

public class OpenAiError extends RuntimeException {
    public OpenAiError(String message, Throwable cause) {
        super(message, cause);
    }

}
