package com.movienow.backend.events;


import com.movienow.backend.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handleUserCreated(String email,
                                  @Header("amqp_receivedRoutingKey") String routingKey) throws IOException {

        System.out.println("Mensaje recibido desde RabbitMQ:");
        System.out.println("Routing key: " + routingKey);
        System.out.println("Email: " + email);

        try {
            emailService.sendWelcomeEmail(email);

        } catch (Exception e) {
            System.err.println("Error al enviar el correo de bienvenida: " + e.getMessage());
        }

    }





}
