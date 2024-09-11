package com.programisto.devis_rapide.application.mail_manager.service.impl;

import com.programisto.devis_rapide.application.mail_manager.entity.Email;
import com.programisto.devis_rapide.application.mail_manager.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String SALES_EMAIL_TEMPLATE_FR = "sales_email_template_fr.html";
    public static final String CLIENT_EMAIL_TEMPLATE_FR = "client_email_template_fr.html";
    public static final String LOGO = "/static/images/programisto.webp";
    public static final String TEXT_HTML_ENCODING = "text/html";

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final TemplateEngine templateEngine;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${app.url}")
    String appUrl;

    @Value("${app.programisto.sales.email}")
    String salesEmailAddress;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendSalesNotificationEmail(Email email) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setFrom("devis.rapide@programisto.fr");
            helper.setTo(salesEmailAddress);
            helper.setSubject("Prospect envoyé quote");

            Context context = new Context();
            context.setVariable("name", "Sales team");
            context.setVariable("app_url", appUrl);
            context.setVariable("action_name", "Le prospect " + (email.to()) + " s'est envoyé le projet à sa adresse e-mail.");
            context.setVariable("image", getContentId(LOGO));
            String text = templateEngine.process(SALES_EMAIL_TEMPLATE_FR, context);

            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource( getResourcePath(LOGO) );
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setHeader("Content-ID", "logo");
            mimeMultipart.addBodyPart(messageBodyPart);

            message.setContent(mimeMultipart);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendClientQuoteEmail(Email email) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setFrom("devis.rapide@programisto.fr");
            helper.setTo(email.to());
            helper.setSubject("Prospect envoyé quote");

            Context context = new Context();
            context.setVariable("name", "Sales team");
            context.setVariable("app_url", appUrl);
            context.setVariable("action_name", "Le prospect s'est envoy&eacture; le projet &agrave; sa adresse e-mail. ");
            context.setVariable("image", getContentId(LOGO));
            String text = templateEngine.process(CLIENT_EMAIL_TEMPLATE_FR, context);

            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource( getResourcePath(LOGO) );
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setHeader("Content-ID", "logo");
            mimeMultipart.addBodyPart(messageBodyPart);

            message.setContent(mimeMultipart);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private MimeMessage getMimeMessage() {
        return mailSender.createMimeMessage();
    }

    private String getContentId(String name) {
        return "<" + name + ">";
    }

    public String getResourcePath(String path) throws IOException {
        return resourceLoader.getResource("classpath:" + path)
                .getFile()
                .getAbsolutePath();
    }

}
