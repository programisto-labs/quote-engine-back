package com.programisto.devis_rapide.application.mail_manager.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import com.programisto.devis_rapide.application.mail_manager.entity.ClientEmailData;
import com.programisto.devis_rapide.domaine.generation.entity.Devis;
import com.programisto.devis_rapide.domaine.generation.entity.projet.Projet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${mailjet.api.key}")
    private String mailjetApiKey;

    @Value("${mailjet.api.secret}")
    private String mailjetApiSecret;

    @Value("${mailjet.api.account}")
    String originEmailAddress;

    @Value("${mailjet.error.account}")
    String errorEmailAddress;

    public EmailService() { }

    @Async
    public void sendEmail(ClientEmailData email) throws Exception {
        ClientOptions options = ClientOptions.builder()
                .apiKey(mailjetApiKey)
                .apiSecretKey(mailjetApiSecret)
                .build();

        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(options);
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put(Emailv31.Message.EMAIL, originEmailAddress)
                                        .put(Emailv31.Message.NAME, "Programisto - Quote Engine"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put(Emailv31.Message.EMAIL, email.getClientEmail())
                                                .put(Emailv31.Message.NAME, email.getClientName()))
                        )
                        .put(Emailv31.Message.TEMPLATEID, 6387948)
                        .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                        .put(Emailv31.Message.TEMPLATEERROR_REPORTING, new JSONObject()
                                .put(Emailv31.Message.EMAIL, errorEmailAddress)
                                .put(Emailv31.Message.NAME, "Quote Engine")
                        )
                        .put(Emailv31.Message.SUBJECT, email.getSubject())
                        .put(Emailv31.Message.TEXTPART, "Devis Rapide")
                        .put(Emailv31.Message.HTMLPART, "<h1>Quote Engine</h1>")
                        .put(Emailv31.Message.VARIABLES, new JSONObject()
                                .put("clientName", email.getClientName())
                                .put("devis", new JSONObject(Devis.toJson( email.getDevis() )))
                                .put("projet", new JSONObject(Projet.toJson( email.getProjet() )))
                        )));

        JSONObject headers = new JSONObject();
        headers.put("X-Mailjet-TrackOpen", "0");
        headers.put("X-Mailjet-TrackClick", "0");
        headers.put("X-Mailjet-DeduplicateCampaign", "1");
        headers.put("X-Mailjet-Prio", "1");
        headers.put("X-Mailjet-SandboxMode", "0");

        request.property(Emailv31.Message.HEADERS, headers);

        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
