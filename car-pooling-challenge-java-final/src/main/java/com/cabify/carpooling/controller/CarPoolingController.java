package com.cabify.carpooling.controller;

import com.cabify.carpooling.controller.dto.CarDTO;
import com.cabify.carpooling.controller.dto.JourneyDTO;
import com.cabify.carpooling.controller.v1.CarController;
import com.cabify.carpooling.controller.v1.JourneyController;
import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the REST controller for Car Pooling Service.
 * This controller is only in charge to receive old endpoints implementation
 * and delegate to new controllers, following the best practices and the logic.
 */
@RestController
public class CarPoolingController {

    @Autowired
    CarController carController;
    @Autowired
    JourneyController journeyController;

    /**
     * PUT method to update the list of cars.
     *
     * @param carsDto a list of CarDTO objects in JSON format.
     * @return ResponseEntity with HTTP status 200 if successful, or 400 if not.
     */
    @PutMapping(value = "/cars", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putCars(@RequestBody List<CarDTO> carsDto) {
        List<Car> cars = new ArrayList<>();
        carsDto.forEach(
                carDTO -> cars.add(carDTO.toCar())
        );

        ResponseEntity<List<Car>> listResponseEntity = carController.deleteAllAndLoadNewCars(cars);
        if (listResponseEntity.hasBody()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * POST method to create a new journey.
     *
     * @param journeyDto: a JourneyDTO object in JSON format.
     * @return ResponseEntity with HTTP status 200.
     */
    @PostMapping("/journey")
    public ResponseEntity<Void> postJourney(@RequestBody @Valid JourneyDTO journeyDto) {
        journeyController.createJourney(journeyDto.toJourney());
        return ResponseEntity.ok().build();
    }

    /**
     * POST method to mark a journey as complete.
     *
     * @param journeyID the ID of the journey to be completed.
     * @return ResponseEntity with HTTP status 200 if successful, or 404 if not.
     */
    @PostMapping(value = "/dropoff",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<Void> postDropoff(@RequestParam("ID") Long journeyID) {
        ResponseEntity<Journey> journeyResponseEntity = journeyController.completeJourney(journeyID);
        if (journeyResponseEntity.hasBody()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST method to locate a car by journey ID.
     *
     * @param journeyID the ID of the journey to locate the car for.
     * @return ResponseEntity with the located Car object in JSON format.
     */
    @PostMapping(
            value = "/locate",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = "application/json")
    public ResponseEntity<Car> postLocate(@RequestParam("ID") Long journeyID) {
        return journeyController.locateCar(journeyID);
    }

}
