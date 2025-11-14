package com.movienow.backend.auth;

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

    public void sendRecoveryEmail(String to, String token) throws IOException {
        Email from = new Email("fiorinamjoaquin@gmail.com");
        String subject = "Recuperar cuenta de Hurry Hand";
        Email toEmail = new Email(to);

        String link = "http://localhost:5173/recover?token="+token;
        Content content = new Content("text/html",
                "<h1>Recuperación de cuenta</h1>"
                        + "<p>Haz clic en el botón para acceder a tu cuenta:</p>"
                        + "<a href='" + link + "' style='background:#4CAF50;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;'>Recuperar acceso</a>");
        Mail mail = new Mail(from, subject, toEmail, content);
        SendGrid sg = new SendGrid(sendgridApiKey);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);

    }


    public void sendWelcomeEmail(String to, String username) throws IOException {
        Email from = new Email("fiorinamjoaquin@gmail.com");
        String subject = "Bienvenido a MovieNow 🎬";
        Email toEmail = new Email(to);

        Content content = new Content("text/html",
                "<h1>¡Hola " + username + "!</h1>" +
                        "<p>Gracias por unirte a <strong>MovieNow</strong>. " +
                        "Ahora podés explorar todas nuestras funciones: comprar funciones, elegir snacks y disfrutar de la experiencia.</p>" +
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