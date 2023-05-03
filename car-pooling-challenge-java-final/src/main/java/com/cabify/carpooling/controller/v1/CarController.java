package com.cabify.carpooling.controller.v1;

import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller to handle requests for endpoints related to Carpooling vehicles.
 */
@RestController
@RequestMapping(value = "/v1/cars", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CarController {

    /**
     * Service used to perform operations related to vehicles.
     */
    @Autowired
    private CarService carService;

    /**
     * Load the list of available cars in the service and remove all previous data (existing journeys and cars).
     *
     * @param cars List of vehicles to update.
     * @return ResponseEntity with the status of the operation.
     * HttpStatus.OK if all vehicles pass the required validations.
     * HttpStatus.BAD_REQUEST if at least one vehicle fails the required validations.
     */
    @PostMapping
    public ResponseEntity<List<Car>> deleteAllAndLoadNewCars(@RequestBody @Valid List<Car> cars) {
        if (cars.stream().allMatch(car ->
                car.getSeats() >= 4
                        && car.getSeats() <= 6
                        && car.getAvailableSeats() >= 0
                        && car.getAvailableSeats() <= car.getSeats()
                        && car.getId() > 0
        )) {

            return ResponseEntity.ok(carService.resetCarsAndJourneys(cars));
        }
        return ResponseEntity.badRequest().build();
    }


}
