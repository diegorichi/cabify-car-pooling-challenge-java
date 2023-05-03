package com.cabify.carpooling.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.service.exception.DuplicatedIdException;

@Service
public class CarPoolingService {

  private List<Car> cars;
  private List<Journey> journeys;
  private List<Journey> pending;


  public void Reset_Cars(List<Car> cars) {
    this.cars = new ArrayList<>();
    this.journeys = new ArrayList<>();
    this.pending = new ArrayList<>();

    for (Car c1: cars) {
      for (Car c2: this.cars) {
        if (c1.getID() == c2.getID()) {
          this.cars = new ArrayList<>();
          this.journeys = new ArrayList<>();
          this.pending = new ArrayList<>();
          throw new DuplicatedIdException("IDs are duplicated");
        }        
        if (c1.getMaxSeats() < 4 || c1.getMaxSeats() > 6) {
          this.cars = new ArrayList<>();
          this.journeys = new ArrayList<>();
          this.pending = new ArrayList<>();
          throw new IllegalArgumentException("invalid seats");
        }
      }
      this.cars.add(c1);
    }
  }

  public void newJourney(Journey journey) {
    Optional<Journey> other = this.journeys
      .stream()
      .filter(j -> j.getId() == journey.getId())
      .findAny();
    if (other.isPresent()) {
      throw new DuplicatedIdException("journey ID is already used");
    }
    Optional<Car> selectedCar = this.cars
      .stream()
      .filter(c -> journey.getPassengers() <= c.getAvailableSeats())
      .findFirst();
    if (selectedCar.isPresent()) {
      Car car = selectedCar.get();
      journey.setAssignedTo(car);
      car.setAvailableSeats(car.getAvailableSeats() - journey.getPassengers());
    } else {
      this.pending.add(journey);
    }
    this.journeys.add(journey);
  }

  public Car dropoff(int journeyID)  {
    Optional<Journey> journey = this.journeys
      .stream()
      .filter(j -> j.getId() == journeyID)
      .findAny();
    if (journey.isEmpty()) {
      throw new NoSuchElementException("ID not found");
    }
    Car car = journey.get().getAssignedTo();
    this.journeys.removeIf(j -> j.getId() == journey.get().getId());
    if (car != null) {
      car.setAvailableSeats(car.getAvailableSeats() + journey.get().getPassengers());      
    } else {
      this.pending.removeIf(j -> j.getId() == journey.get().getId());
    }
    return car;
  }

  public void reassign(Car car) {
    Optional<Journey> journey = this.pending
      .stream()
      .filter(j -> j.getPassengers() <= car.getAvailableSeats())
      .findFirst();
    journey.ifPresent(j -> {
      System.out.format(">> Car %d reassigned to journey %d\n", car.getID(), j.getId());
      j.setAssignedTo(car);
      car.setAvailableSeats(car.getAvailableSeats() - j.getPassengers());
      this.pending.removeIf(j2 -> j2.getId() == j.getId());
    });
  }

  public Car locate(int journeyID) {
    Optional<Journey> journey = this.journeys
      .stream()
      .filter(j -> j.getId() == journeyID)
      .findAny();
    if (journey.isEmpty()) {
      throw new NoSuchElementException("journey not found");
    }
    return journey.get().getAssignedTo();
  }

  private Optional<Car> findCar(int seats) {
    return this.cars.stream()
      .filter(c -> c.getAvailableSeats() >= seats)
      .findAny();
  }
}
