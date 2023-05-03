package com.cabify.carpooling.repository;

import com.cabify.carpooling.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Car entities.
 */
@Repository
public interface CarRepository extends PagingAndSortingRepository<Car, Long> {

    /**
     * Finds all cars with available seats greater than or equal to the specified value,
     * ordered by available seats in ascending order.
     *
     * @param availableSeats the minimum number of available seats
     * @return a list of Car entities
     */
    List<com.cabify.carpooling.model.Car> findByAvailableSeatsGreaterThanEqualOrderByAvailableSeatsAsc(int availableSeats);

}

