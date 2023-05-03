package com.cabify.carpooling.controller.dto;


import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JourneyDTO implements Serializable {

    private Long id;
    @Min(value = 1, message = "The journey should have at least one passenger")
    @Max(value = 6, message = "The journey can have up to 6 passengers")
    private int people;

    private Long assignedToId;

    private Date createdDate;

    private boolean completed = false;

    public Journey toJourney() {
        Journey journey = new Journey();
        journey.setId(this.id);
        journey.setPeople(this.people);
        journey.setCompleted(this.completed);

        if (this.assignedToId != null) {
            Car car = new Car();
            car.setId(this.assignedToId);
            journey.setAssignedTo(car);
        }

        return journey;
    }
}