package com.cabify.carpooling.controller;

import com.cabify.carpooling.message.MessageListener;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.service.AssignmentService;
import com.cabify.carpooling.service.JourneyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RabbitListenerTest
@AutoConfigureMockMvc
class JourneyControllerTests {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    JourneyService journeyService;
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
    Journey journey;
    Journey journey3;
    Journey journeyInvalid;

    @BeforeEach
    void setUp() {
        journey = new Journey(1L, 4, null, new Date(), false);
        journey3 = new Journey(3L, 4, null, new Date(), false);
        journeyInvalid = new Journey(4L, 12, null, new Date(), false);

    }

    @Test
    void testInvalidParameter(@Autowired MockMvc mvc) throws Exception {
        String invalidJson = "{'invalid':1,'content',}";
        mvc.perform(put("/cars").contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("no_ID=" + journey3.getId()))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/dropoff").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("NotValid=" + journey3.getId()))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testInvalidMethod(@Autowired MockMvc mvc) throws Exception {

        mvc.perform(put("/cars").contentType(MediaType.TEXT_PLAIN)
                        .content("invalidJson"))
                .andExpect(status().isUnsupportedMediaType());

        mvc.perform(post("/locate").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journey)))
                .andExpect(status().isUnsupportedMediaType());

        mvc.perform(post("/dropoff").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journey)))
                .andExpect(status().isUnsupportedMediaType());

    }

    @Test
    void invalidArgumentTest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/journey").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journeyInvalid)))
                .andExpect(status().isBadRequest());

    }

}
