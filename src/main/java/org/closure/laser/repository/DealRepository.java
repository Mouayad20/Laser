package org.closure.laser.repository;

import java.util.Date;
import java.util.List;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.DealStatus;
import org.closure.laser.domain.Trip;
import org.closure.laser.domain.UserApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    Page<Deal> findByTrip(Trip trip, Pageable pageable);

    List<Deal> findByDeliver(UserApplication deliver, Pageable pageable);

    List<Deal> findByOwner(UserApplication owner, Pageable pageable);

    Page<Deal> findByStatus(DealStatus status, Pageable pageable);

    @Query(value = "SELECT * FROM deal WHERE deliver_id=:d_id AND trip_id=:t_id", nativeQuery = true)
    List<Deal> findByDeliverAndTrip(@Param("d_id") Long deliver_id, @Param("t_id") Long trip_id);

    @Query(
        value = "SELECT * FROM deal         " +
        "        WHERE id IN(               " +
        "                   SELECT deal_id  " +
        "                   FROM   shipment " +
        "                   WHERE `created_at` >= NOW() - INTERVAL 10 DAY )",
        nativeQuery = true
    )
    Page<Deal> findLast10DaysShipments(Pageable pageable);

    @Query(
        value = "SELECT * FROM deal " +
        "WHERE owner_id IS NULL " +
        "AND trip_id IN(" +
        " SELECT id FROM trip " +
        "WHERE `created_at` >= NOW() - INTERVAL 10 DAY )",
        nativeQuery = true
    )
    List<Deal> findLast10DaysTrips(Pageable pageable);

    @Query(value = "SELECT * FROM deal WHERE from_account LIKE %:value%", nativeQuery = true)
    Page<Deal> search(@Param(value = "value") String value, Pageable pageable);

    @Query(
        value = "SELECT * FROM deal " +
        "WHERE owner_id IS NULL " +
        "AND `available_weight` >= :weight " +
        "AND `arrivel_date`     <= :date " +
        "AND  trip_id IN(" +
        "               SELECT id FROM trip" +
        "               WHERE  from_id IN (SELECT id FROM location " +
        "                                   WHERE  `country` LIKE %:from% ) " +
        "               AND      to_id IN (SELECT id FROM location " +
        "                                   WHERE  `country` LIKE %:to%   )" +
        "               )",
        nativeQuery = true
    ) //
    List<Deal> searchTrips(
        @Param(value = "from") String from,
        @Param(value = "to") String to,
        @Param(value = "weight") Double weight,
        @Param(value = "date") Date date,
        Pageable pageable
    );

    @Query(
        value = "SELECT * FROM deal " +
        "WHERE `available_weight` >= :weight " +
        "AND   `expected_date`    <= :date " +
        "AND    id IN(" +
        "              SELECT deal_id FROM shipment" +
        "               WHERE from_id IN (SELECT id FROM location       " +
        "                                 WHERE country LIKE %:from%   )" +
        "               AND   to_id   IN (SELECT id FROM location       " +
        "                                 WHERE country LIKE %:to%      )" +
        "             )",
        nativeQuery = true
    )
    List<Deal> searchShipments(
        @Param(value = "from") String from,
        @Param(value = "to") String to,
        @Param(value = "weight") Double weight,
        @Param(value = "date") Date date,
        Pageable pageable
    );
}
