package com.cabify.carpooling.controller;

import com.cabify.carpooling.message.MessageListener;
import com.cabify.carpooling.model.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RabbitListenerTest
@AutoConfigureMockMvc
class CarControllerTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    RabbitTemplate rabbitTemplate;
    @MockBean
    ConnectionFactory connectionFactory;
    @MockBean
    Connection connection;
    @MockBean
    MessageListener messageListener;
    List<Car> cars;
    List<Car> carsValid;

    @BeforeEach
    void setUp() {
        Mockito.when(rabbitTemplate.getConnectionFactory()).thenReturn(connectionFactory);
        Mockito.when(rabbitTemplate.getConnectionFactory().createConnection()).thenReturn(connection);
        Mockito.when(connectionFactory.createConnection()).thenReturn(connection);

        Car car1 = new Car(1L, 4, -1);
        Car car2 = new Car(1L, 6, 12);
        cars = List.of(car1, car2);
        Car car3 = new Car(1L, 4, 4);
        Car car4 = new Car(1L, 6, 6);
        carsValid = List.of(car3, car4);

    }

    @Test
    void argumentNotValidTest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(put("/cars").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cars)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putValidCars(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(put("/cars").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carsValid)))
                .andExpect(status().isOk());
    }
}
