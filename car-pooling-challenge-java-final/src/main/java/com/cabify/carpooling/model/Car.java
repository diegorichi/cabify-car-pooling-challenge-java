package com.cabify.carpooling.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Car implements Serializable {
    @Id
    private Long id;

    @Min(value = 4, message = "The car should have at least 4 seats")
    @Max(value = 6, message = "The car should have up to 6 seats")
    private int seats;

    @Min(0)
    @Max(6)
    private int availableSeats;

}
