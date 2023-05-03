package com.cabify.carpooling.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RabbitListenerTest
@AutoConfigureMockMvc
class StatusControllerTests {

    @MockBean
    RabbitTemplate rabbitTemplate;
    @MockBean
    ConnectionFactory connectionFactory;
    @MockBean
    Connection connection;

    @Test
    void okStatusTest(@Autowired MockMvc mvc) throws Exception {
        Mockito.when(rabbitTemplate.getConnectionFactory()).thenReturn(connectionFactory);
        Mockito.when(rabbitTemplate.getConnectionFactory().createConnection()).thenReturn(connection);
        Mockito.when(rabbitTemplate.getConnectionFactory().createConnection().isOpen()).thenReturn(true);
        mvc.perform(get("/status")).andExpect(status().isOk());
    }

    @Test
    void notOkStatusOnCreateConnection(@Autowired MockMvc mvc) throws Exception {
        Mockito.when(rabbitTemplate.getConnectionFactory()).thenReturn(connectionFactory);
        Mockito.when(rabbitTemplate.getConnectionFactory().createConnection()).thenThrow(new AmqpException("connection refused"));
        mvc.perform(get("/status")).andExpect(status().isServiceUnavailable());
    }

    @Test
    void notOkStatusOnIsOpen(@Autowired MockMvc mvc) throws Exception {
        Mockito.when(rabbitTemplate.getConnectionFactory()).thenReturn(connectionFactory);
        Mockito.when(rabbitTemplate.getConnectionFactory().createConnection()).thenReturn(connection);
        Mockito.when(rabbitTemplate.getConnectionFactory().createConnection().isOpen()).thenReturn(false);
        mvc.perform(get("/status")).andExpect(status().isServiceUnavailable());
    }

}
