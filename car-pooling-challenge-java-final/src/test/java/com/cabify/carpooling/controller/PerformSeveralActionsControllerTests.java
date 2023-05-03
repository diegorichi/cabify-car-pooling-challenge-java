package com.cabify.carpooling.controller;

import com.cabify.carpooling.message.MessageListener;
import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.service.AssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RabbitListenerTest
@AutoConfigureMockMvc
class PerformSeveralActionsControllerTests {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    AssignmentService assignmentService;
    @MockBean
    RabbitTemplate rabbitTemplate;
    @MockBean
    ConnectionFactory connectionFactory;
    @MockBean
    Connection connection;
    @MockBean
    MessageListener messageListener;
    List<Car> cars;
    Journey journey;
    Journey journey2;
    Journey journey3;
    Journey journeyInvalid;

    @BeforeEach
    void setUp() {
        Mockito.when(rabbitTemplate.getConnectionFactory()).thenReturn(connectionFactory);
        Mockito.when(rabbitTemplate.getConnectionFactory().createConnection()).thenReturn(connection);
        Mockito.when(connectionFactory.createConnection()).thenReturn(connection);

        Car car1 = new Car(1L, 4, 4);
        Car car2 = new Car(1L, 6, 6);
        cars = List.of(car1, car2);
        journey = new Journey(1L, 4, null, new Date(), false);
        journey2 = new Journey(2L, 6, null, new Date(), false);
        journey3 = new Journey(3L, 4, null, new Date(), false);
        journeyInvalid = new Journey(4L, 12, null, new Date(), false);

        Mockito.doAnswer((Answer<Void>) invocation -> {
            Journey j1 = (Journey) Arrays.stream(invocation.getArguments()).findFirst().get();
            assignmentService.assign(j1);
            return null;
        }).when(messageListener).publish(Mockito.any(Journey.class));

        Mockito.doAnswer((Answer<Void>) invocation -> {
            Car c1 = (Car) Arrays.stream(invocation.getArguments()).findFirst().get();
            assignmentService.reassign(c1);
            return null;
        }).when(messageListener).publish(Mockito.any(Car.class));

    }

    @Test
    void performSeveralActionsTest(@Autowired MockMvc mvc) throws Exception {

        mvc.perform(put("/cars").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cars)))
                .andExpect(status().isOk());

        mvc.perform(post("/journey").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journey)))
                .andExpect(status().isOk());

        mvc.perform(post("/journey").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journey2)))
                .andExpect(status().isOk());

        mvc.perform(post("/journey").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journey3)))
                .andExpect(status().isOk());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey.getId()))
                .andExpect(status().isOk());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey2.getId()))
                .andExpect(status().isNoContent());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey3.getId()))
                .andExpect(status().isNoContent());

        mvc.perform(post("/dropoff").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey.getId()))
                .andExpect(status().isOk());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey.getId()))
                .andExpect(status().isNotFound());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey3.getId()))
                .andExpect(status().isOk());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey2.getId()))
                .andExpect(status().isNoContent());

        mvc.perform(post("/dropoff").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey3.getId()))
                .andExpect(status().isOk());

        mvc.perform(post("/dropoff").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey3.getId()))
                .andExpect(status().isNotFound());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("ID=" + journey2.getId()))
                .andExpect(status().isOk());

    }

}
