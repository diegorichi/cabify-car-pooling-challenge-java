package com.cabify.carpooling.service;

import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.repository.CarRepository;
import com.cabify.carpooling.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    @Autowired
    JourneyRepository journeyRepository;

    @Autowired
    CarRepository carRepository;

    /**
     * Assigns a journey to the first available car with enough seats.
     * If the journey is already assigned, it does nothing.
     * If no available car is found, it does nothing.
     *
     * @param journey the journey to be assigned.
     */
    public void assign(Journey journey) {
        if (journey.getAssignedTo() == null) {
            carRepository.findByAvailableSeatsGreaterThanEqualOrderByAvailableSeatsAsc(journey.getPeople())
                    .stream().findFirst()
                    .ifPresent(car ->
                            {
                                journey.setAssignedTo(car);
                                car.setAvailableSeats(car.getAvailableSeats() - journey.getPeople());
                                journeyRepository.save(journey);
                                carRepository.save(car);
                            }
                    );
        }
    }

    /**
     * Reassigns all journeys previously assigned to a car with not enough available seats to the given car.
     *
     * @param car the car to which the journeys will be reassigned.
     */
    public void reassign(Car car) {
        if (car.getSeats() >= car.getAvailableSeats()
                && car.getAvailableSeats() > 0) {
            journeyRepository.findAllByAssignedToIsNullOrderByCreatedDateDesc()
                    .forEach(journey -> {
                        if (car.getAvailableSeats() >= journey.getPeople()) {
                            journey.setAssignedTo(car);
                            car.setAvailableSeats(car.getAvailableSeats() - journey.getPeople());
                            carRepository.save(car);
                            journeyRepository.save(journey);
                        }
                    });
        }
    }

}
