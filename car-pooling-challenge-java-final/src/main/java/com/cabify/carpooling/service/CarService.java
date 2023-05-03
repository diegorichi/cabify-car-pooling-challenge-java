package com.cabify.carpooling.service;

import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.repository.CarRepository;
import com.cabify.carpooling.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarService {

    @Autowired
    CarRepository carRepository;
    @Autowired
    JourneyRepository journeyRepository;

    /**
     * Resets the available seats for all cars and deletes all journeys.
     *
     * @param cars the list of cars to reset
     * @return the list of updated cars
     */
    public List<Car> resetCarsAndJourneys(List<Car> cars) {
        journeyRepository.deleteAll();
        carRepository.deleteAll();

        cars.forEach(car ->
                car.setAvailableSeats(car.getSeats())
        );
        return new ArrayList<>((Collection<? extends Car>) carRepository.saveAll(cars));
    }

}
