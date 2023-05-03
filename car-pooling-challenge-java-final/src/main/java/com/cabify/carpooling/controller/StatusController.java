package com.cabify.carpooling.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for checking the status of the RabbitMQ connection.
 */
@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Endpoint for checking the status of the CarPooling.
     * The carpooling is ready when the RabbitMQ connection is successful.
     *
     * @return A {@link ResponseEntity} with HTTP status 200 OK if the connection is open, or 503 SERVICE UNAVAILABLE
     * if there is an error connecting to RabbitMQ.
     */
    @GetMapping
    public ResponseEntity<HttpStatus> getStatus() {
        try (Connection connection = rabbitTemplate.getConnectionFactory().createConnection()) {
            if (connection.isOpen()) {
                return ResponseEntity.ok().build();
            }
        } catch (AmqpException ae) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}
