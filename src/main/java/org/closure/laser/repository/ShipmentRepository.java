package org.closure.laser.repository;

import java.util.List;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.ShipmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Shipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Page<Shipment> findByType(ShipmentType type, Pageable pageable);

    Page<Shipment> findByDeal(Deal deal, Pageable pageable);

    List<Shipment> findByDeal(Deal deal);

    @Query(value = "SELECT * FROM shipment WHERE :key LIKE '%:value%'", nativeQuery = true)
    Page<Shipment> search(@Param(value = "key") String key, @Param(value = "value") String value, Pageable pageable);

    @Query(
        value = "SELECT * FROM shipment" +
        " WHERE from_id IN (SELECT id FROM location " +
        "                WHERE  country LIKE %:value% " +
        "                OR     city    LIKE %:value% " +
        "                OR     airport LIKE %:value% ) ",
        nativeQuery = true
    )
    Page<Shipment> searchFrom(@Param(value = "value") String value, Pageable pageable);

    @Query(
        value = "SELECT * FROM shipment" +
        " WHERE to_id IN (SELECT id FROM location " +
        "                WHERE  country LIKE %:value% " +
        "                OR     city    LIKE %:value% " +
        "                OR     airport LIKE %:value% ) ",
        nativeQuery = true
    )
    Page<Shipment> searchTo(@Param(value = "value") String value, Pageable pageable);
}
