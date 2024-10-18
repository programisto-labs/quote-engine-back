package com.programisto.devis_rapide.application.mail_manager;


import com.programisto.devis_rapide.application.entity.HttpResponse;
import com.programisto.devis_rapide.application.mail_manager.entity.ClientEmailData;
import com.programisto.devis_rapide.application.mail_manager.service.EmailService;
import com.programisto.devis_rapide.application.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/devis")
public class MailController {
    @Autowired
    private final EmailService emailService;

    @PostMapping(value = "/sendEmail", consumes = "application/json;charset=UTF-8")
    public ResponseEntity<HttpResponse> sendEmail(@RequestBody ClientEmailData email) {
        try {
            validateClientEmailParams(email);
            emailService.sendEmail(email);
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message("Client email message sent successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
    }

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    private void validateClientEmailParams(ClientEmailData email) throws MissingRequestValueException {
        if (!Util.isValid(email.getClientEmail())) {
            throw new MissingRequestValueException("The destination address is required");
        }
        if (!Util.isValid(email.getClientName())) {
            throw new MissingRequestValueException("The client name is required");
        }
        if (Objects.isNull(email.getDevis())) {
            throw new MissingRequestValueException("The devis data is required");
        }
        if (Objects.isNull(email.getProjet())) {
            throw new MissingRequestValueException("The projet data is required");
        }
    }
}
