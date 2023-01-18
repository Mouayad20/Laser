package org.closure.laser.repository;

import java.util.Optional;
import org.closure.laser.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByCity(String city);

    @Query(
        value = "SELECT * FROM location " +
        "WHERE  country LIKE %:word% " +
        "OR     city    LIKE %:word% " +
        "OR     airport LIKE %:word% ",
        nativeQuery = true
    )
    Page<Location> search(@Param(value = "word") String word, Pageable pageable);

    @Query(value = "SELECT * FROM location WHERE :key LIKE '%:value%'", nativeQuery = true)
    Page<Location> search(@Param(value = "key") String key, @Param(value = "value") String value, Pageable pageable);

    Optional<Location> findByAirport(String airport);
}
