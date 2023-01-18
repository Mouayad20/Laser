package org.closure.laser.repository;

import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Offers;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Offers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OffersRepository extends JpaRepository<Offers, Long> {
    Optional<Offers> findByShipmentDealIdAndTripDealId(Long shipmentDealId, Long tripDealId);

    List<Offers> findAllByShipmentDealId(Long shipmentDealId);

    List<Offers> findAllByTripDealId(Long tripDealId);

    List<Offers> findByShipmentDealId(Long shipmentDealId);

    Optional<Offers> findByTripDealIdAndShipmentDealId(Long tripDealId, Long shipmentDealId);

    List<Offers> findByTripDealId(Long tripDealId);
}
