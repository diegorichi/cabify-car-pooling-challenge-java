package com.cabify.carpooling.repository;

import com.cabify.carpooling.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on Journey entities.
 */
@Repository
public interface JourneyRepository extends PagingAndSortingRepository<Journey, Long> {

    /**
     * Returns a list of all unassigned journeys in descending order of their creation dates.
     *
     * @return a list of unassigned journeys
     */
    List<Journey> findAllByAssignedToIsNullOrderByCreatedDateDesc();

    /**
     * Returns the Journey entity with the given ID if it is not yet completed and not assigned to a car.
     *
     * @param journeyId the ID of the journey to retrieve
     * @return an Optional containing the Journey entity with the given ID, or an empty Optional if no such entity exists or it is completed or already assigned to a car
     */
    Optional<Journey> findByCompletedFalseAndId(Long journeyId);
}
