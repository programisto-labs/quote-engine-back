package com.programisto.devis_rapide.application.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;


@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {
    protected String timestamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String message;
    protected String devMessage;
    protected String path;
    protected String requestMethod;
    protected Map<?, ?> data;
}
