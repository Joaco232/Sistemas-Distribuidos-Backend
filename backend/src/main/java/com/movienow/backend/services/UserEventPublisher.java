package com.movienow.backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    // El evento es best-effort: si RabbitMQ no esta levantado no se corta el registro
    // del usuario, solo se pierde el mail de bienvenida.
    public void publishUserCreated(String email) {

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, email);
        } catch (Exception e) {
            log.warn("No se pudo publicar el evento user.created para {}: {}", email, e.getMessage());
        }
    }









}
