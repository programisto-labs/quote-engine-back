package com.programisto.devis_rapide.application.mail_manager.service;

import com.programisto.devis_rapide.application.mail_manager.entity.ClientEmailBody;
import com.programisto.devis_rapide.application.mail_manager.entity.Email;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

@Service
public class EmailService {
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String SALES_EMAIL_TEMPLATE_FR = "sales_email_template_fr.html";
    public static final String CLIENT_EMAIL_TEMPLATE_FR = "client_email_template_fr.html";
    public static final String LOGO = "/static/images/programisto.jpg";
    public static final String TEXT_HTML_ENCODING = "text/html";

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final TemplateEngine templateEngine;

    @Value("${app.url}")
    String appUrl;

    @Value("${spring.mail.username}")
    String originEmailAddress;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendSalesNotificationEmail(Email email) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setFrom(originEmailAddress);
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());

            Context context = new Context(Locale.FRANCE);
            context.setVariable("htmlUtils", new HtmlHelper());
            context.setVariable("name", "Sales team");
            context.setVariable("app_url", appUrl);
            context.setVariable("action_name", email.getBody());
            String text = templateEngine.process(SALES_EMAIL_TEMPLATE_FR, context);

            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

            mimeMultipart.addBodyPart(logoBodyPart());

            message.setContent(mimeMultipart);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendClientQuoteEmail(ClientEmailBody email) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setFrom(originEmailAddress);
            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());

            Context context = new Context();
            context.setVariable("htmlUtils", new HtmlHelper());
            context.setVariable("devis", email.getDevis());
            context.setVariable("projet", email.getProjet());
            String text = templateEngine.process(CLIENT_EMAIL_TEMPLATE_FR, context);

            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

            mimeMultipart.addBodyPart(logoBodyPart());

            message.setContent(mimeMultipart);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public MimeBodyPart logoBodyPart() throws MessagingException, IOException {
        MimeBodyPart logoPart = new MimeBodyPart();
        InputStream imageStream = new ClassPathResource(LOGO).getInputStream();

        logoPart.setDataHandler(new DataHandler(new ByteArrayDataSource(imageStream, "image/jpg")));
        logoPart.setFileName("programisto.jpg");
        logoPart.setHeader("Content-ID", "logo");

        return logoPart;
    }

    private MimeMessage getMimeMessage() {
        return mailSender.createMimeMessage();
    }
}
