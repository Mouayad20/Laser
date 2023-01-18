package org.closure.laser.repository;

import java.util.Optional;
import org.closure.laser.domain.ShipmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ShipmentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentTypeRepository extends JpaRepository<ShipmentType, Long> {
    Optional<ShipmentType> findByName(String name);

    @Query(value = "SELECT * FROM shipment_type WHERE :key LIKE '%:value%'", nativeQuery = true)
    Page<ShipmentType> search(@Param(value = "key") String key, @Param(value = "value") String value, Pageable pageable);
}
