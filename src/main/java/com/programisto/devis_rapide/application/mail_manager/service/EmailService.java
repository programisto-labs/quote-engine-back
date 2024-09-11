package com.programisto.devis_rapide.application.mail_manager.service;

import com.programisto.devis_rapide.application.mail_manager.entity.Email;

public interface EmailService {
    void sendSalesNotificationEmail(Email email);
    void sendClientQuoteEmail(Email email);
}
