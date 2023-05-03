package com.cabify.carpooling.controller.dto;


import com.cabify.carpooling.model.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarDTO implements Serializable {

    private Long id;

    @Min(value = 4, message = "The car should have at least 4 seats")
    @Max(value = 6, message = "The car should have up to 6 seats")
    private int seats;

    @Min(0)
    @Max(6)
    private int availableSeats;

    public Car toCar() {
        Car car = new Car();
        car.setId(this.id);
        car.setSeats(this.seats);
        car.setAvailableSeats(this.availableSeats);
        return car;
    }
}