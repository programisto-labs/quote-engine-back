package com.programisto.devis_rapide.application.mail_manager;


import com.programisto.devis_rapide.application.mail_manager.entity.ClientEmailBody;
import com.programisto.devis_rapide.application.mail_manager.entity.Email;
import com.programisto.devis_rapide.application.mail_manager.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devis/mail")
public class MailController {
    @Autowired
    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/toClient")
    public void sendToClient(@RequestBody ClientEmailBody email) {
            emailService.sendClientQuoteEmail(email);
    }

    @PostMapping("/toSales")
    public void sendToSales(@RequestBody Email email) {
        emailService.sendSalesNotificationEmail(email);
    }
}
