package com.cabify.carpooling.controller.v1;

import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.NoSuchElementException;


/**
 * Controller class that handles requests related to journeys.
 */
@RestController
@RequestMapping(value = "/v1/journeys", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class JourneyController {

    @Autowired
    private JourneyService journeyService;

    /**
     * Handles the creation of a new journey by receiving a POST request with a {@link Journey} object as request body,
     * validates it with the @Valid annotation and saves it using the journeyService.
     *
     * @param journey The {@link Journey} object to be created and saved.
     * @return A {@link ResponseEntity} with status 200 OK if the journey was created and saved successfully.
     * @throws MethodArgumentNotValidException If the {@link Journey} object fails validation.
     */
    @PostMapping
    public ResponseEntity<Journey> createJourney(@RequestBody @Valid Journey journey) {
        return ResponseEntity.ok(journeyService.createJourney(journey));
    }

    /**
     * Completes a  {@link Journey} with the given ID.
     *
     * @param id the ID of the journey to complete.
     * @return a {@link ResponseEntity} with the completed journey.
     * Or 404 Not found if the journey is not in the database.
     */
    @PostMapping(value = "{id}/complete")
    public ResponseEntity<Journey> completeJourney(@PathVariable("id") @Positive Long id) {
        Journey journey = journeyService.complete(id);
        if (journey == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(journey);
    }

    /**
     * Locates the {@link Car} associated with the  {@link Journey} with the given ID.
     *
     * @param id the ID of the journey to locate the car for.
     * @return a {@link ResponseEntity} with the located car.
     * If the journey isn't present, return 404 Not found.
     * If the journey is waiting, then return 204 No Content.
     */
    @GetMapping(value = "{id}/locate")
    public ResponseEntity<Car> locateCar(@PathVariable("id") @Positive Long id) {
        try {
            Car locatedCar = journeyService.locate(id);
            if (locatedCar == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(locatedCar);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
