package com.programisto.devis_rapide.application.mail_manager;


import com.programisto.devis_rapide.application.mail_manager.entity.ClientEmailBody;
import com.programisto.devis_rapide.application.mail_manager.entity.Email;
import com.programisto.devis_rapide.application.mail_manager.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devis/mail")
public class MailController {
    private final EmailService emailService;

    @Autowired
    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/toClient")
    public void sendEmail(@RequestBody ClientEmailBody email) {
            emailService.sendSalesNotificationEmail(new Email(email.to(), email.subject(), "emptyBody"));
            emailService.sendClientQuoteEmail(email);
    }
}
