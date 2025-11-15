package com.movienow.backend.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String sendgridApiKey;

    public void sendWelcomeEmail(String to) throws IOException {
        Email from = new Email("fiorinamjoaquin@gmail.com");
        String subject = "Bienvenido a MovieNow 🎬";
        Email toEmail = new Email(to);

        Content content = new Content("text/html",
                "<h1>¡Hola!</h1>" +
                        "<p>Gracias por unirte a <strong>MovieNow</strong>. " +
                        "Ahora podés encontrar cualquier peli y en que lugar verla.</p>" +
                        "<p>¡Nos encanta tenerte acá! 🍿🎥</p>");

        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }




}