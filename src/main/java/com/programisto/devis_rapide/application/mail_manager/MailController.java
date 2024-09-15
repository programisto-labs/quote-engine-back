package com.programisto.devis_rapide.application.mail_manager;


import com.programisto.devis_rapide.application.entity.HttpResponse;
import com.programisto.devis_rapide.application.mail_manager.entity.ClientEmailBody;
import com.programisto.devis_rapide.application.mail_manager.entity.Email;
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
@RequestMapping("/devis/mail")
public class MailController {
    @Autowired
    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/toClient")
    public ResponseEntity<HttpResponse> sendToClient(@RequestBody ClientEmailBody email) {
        System.out.println(email.getTo());
        System.out.println(email.getSubject());
        System.out.println(email.getDevis());
        System.out.println(email.getProjet());
        try {
            validateClientEmailParams(email);
            emailService.sendClientQuoteEmail(email);
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message("Client email message sent successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
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

    @PostMapping("/toSales")
    public ResponseEntity<HttpResponse>sendToSales(@RequestBody Email email) {
        try {
            validateEmailParams(email);
            emailService.sendSalesNotificationEmail(email);
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timestamp(LocalDateTime.now().toString())
                            .message("Sales email message sent successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
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

    private void validateEmailParams(Email email) throws MissingRequestValueException {
        if (!Util.isValid(email.getTo())) {
            throw new MissingRequestValueException("The destination address is required");
        }
        if (!Util.isValid(email.getSubject())) {
            throw new MissingRequestValueException("The subject is required");
        }
        if (!Util.isValid(email.getBody())) {
            throw new MissingRequestValueException("The body message is required");
        }
    }

    private void validateClientEmailParams(ClientEmailBody email) throws MissingRequestValueException {
        if (!Util.isValid(email.getTo())) {
            throw new MissingRequestValueException("The destination address is required");
        }
        if (!Util.isValid(email.getSubject())) {
            throw new MissingRequestValueException("The subject is required");
        }
        if (Objects.isNull(email.getDevis())) {
            throw new MissingRequestValueException("The devis data is required");
        }
        if (Objects.isNull(email.getProjet())) {
            throw new MissingRequestValueException("The projet data is required");
        }
    }
}
