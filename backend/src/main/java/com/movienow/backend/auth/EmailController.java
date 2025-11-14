package com.movienow.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/mail")
    public String sendMail() throws IOException {
        emailService.sendRecoveryEmail("santbord@gmail.com", "TOKEN_DE_PRUEBA");
        return "Correo enviado!";
    }

}

