package com.movienow.backend.events;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handleUserCreated(String email,
                                  @Header("amqp_receivedRoutingKey") String routingKey) {

        System.out.println("Mensaje recibido desde RabbitMQ:");
        System.out.println("Routing key: " + routingKey);
        System.out.println("Email: " + email);

        sendWelcomeEmail(email);
    }

    private void sendWelcomeEmail(String email) {
        System.out.println("Enviando mail de bienvenida a " + email);
    }



}
