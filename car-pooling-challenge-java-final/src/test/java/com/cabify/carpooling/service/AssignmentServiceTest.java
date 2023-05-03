package com.cabify.carpooling.service;

import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.repository.JourneyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class AssignmentServiceTest {

    @Autowired
    AssignmentService assignmentService;
    @MockBean
    JourneyRepository journeyRepository;

    Journey journey;


    Car car;

    @BeforeEach
    void setUp() {
        journey = new Journey(1L, 4, null, new Date(), false);
        car = new Car(1L, 4, 4);
    }

    @Test
    void reassignTest() {
        List list = new ArrayList();
        list.add(journey);
        Mockito.when(journeyRepository.findAllByAssignedToIsNullOrderByCreatedDateDesc())
                .thenReturn(list);

        assignmentService.reassign(car);

        Assertions.assertNotNull(journey.getAssignedTo());
        Assertions.assertEquals(0, car.getAvailableSeats());
    }

}
