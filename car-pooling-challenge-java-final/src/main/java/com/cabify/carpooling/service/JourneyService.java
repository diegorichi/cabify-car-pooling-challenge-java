package com.cabify.carpooling.service;

import com.cabify.carpooling.message.MessageListener;
import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.repository.CarRepository;
import com.cabify.carpooling.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class JourneyService {

    @Autowired
    JourneyRepository journeyRepository;
    @Autowired
    CarRepository carRepository;

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    MessageListener messageListener;

    /**
     * Creates a new journey and assigns a car to it if possible. It also publishes a message to notify the creation of the journey.
     *
     * @param journey the journey to be created.
     * @return the journey created with a possible assigned car and published message.
     */
    public Journey createJourney(Journey journey) {

        Journey createdJourney = journeyRepository.save(journey);
        assignmentService.assign(createdJourney);
        messageListener.publish(createdJourney);

        return createdJourney;
    }

    /**
     * Completes the journey with the given journey ID and returns the completed journey object.
     *
     * @param journeyId the ID of the journey to complete
     * @return the completed journey object, or null if the journey was not found or is already completed
     */
    public Journey complete(Long journeyId) {

        Optional<Journey> optionalJourney = journeyRepository.findByCompletedFalseAndId(journeyId);

        if (optionalJourney.isEmpty()) return null;

        Journey journey = optionalJourney.get();

        Car car = journey.getAssignedTo();
        if (car != null) {
            car.setAvailableSeats(car.getAvailableSeats() + journey.getPeople());
            carRepository.save(car);
            handleReleaseSeats(car);
        }

        journey.setCompleted(true);
        journeyRepository.save(journey);

        return journey;
    }

    private void handleReleaseSeats(Car car) {
        if (car.getAvailableSeats() > 0) {
            assignmentService.reassign(car);
            messageListener.publish(car);
        }
    }

    /**
     * Retrieves the car assigned to the specified journey that has not been completed yet.
     *
     * @param journeyId the ID of the journey
     * @return the car assigned to the journey
     * @throws NoSuchElementException if the journey does not exist or has already been completed
     */
    public Car locate(Long journeyId) {
        Optional<Journey> optionalJourney = journeyRepository.findByCompletedFalseAndId(journeyId);
        if (optionalJourney.isEmpty())
            throw new NoSuchElementException("Journey not found or already completed");

        Journey journey = optionalJourney.get();
        return journey.getAssignedTo();
    }

}
