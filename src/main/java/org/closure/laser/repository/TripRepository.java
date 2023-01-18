package org.closure.laser.repository;

import java.util.Optional;
import org.closure.laser.domain.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByTripIdentifier(String tripIdentifier);

    @Query(value = "SELECT * FROM trip WHERE trip_identifier LIKE %:value% ", nativeQuery = true)
    Page<Trip> search(@Param(value = "value") String value, Pageable pageable);

    @Query(
        value = "SELECT * FROM trip" +
        " WHERE from_id IN (SELECT id FROM location " +
        "                WHERE  country LIKE %:value% " +
        "                OR     city    LIKE %:value% " +
        "                OR     airport LIKE %:value% ) ",
        nativeQuery = true
    )
    Page<Trip> searchFrom(@Param(value = "value") String value, Pageable pageable);

    @Query(
        value = "SELECT * FROM trip" +
        " WHERE to_id IN (SELECT id FROM location " +
        "                WHERE  country LIKE %:value% " +
        "                OR     city    LIKE %:value% " +
        "                OR     airport LIKE %:value% ) ",
        nativeQuery = true
    )
    Page<Trip> searchTo(@Param(value = "value") String value, Pageable pageable);
}
