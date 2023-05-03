package com.cabify.carpooling;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point of the CarPooling application.
 * <p>
 * The application is a Spring Boot application that enables JPA Auditing and RabbitMQ.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableRabbit
public class CarPoolingApplication {

    /**
     * The main method that launches the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(CarPoolingApplication.class, args);
    }

}
