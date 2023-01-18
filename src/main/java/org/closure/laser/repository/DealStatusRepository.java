package org.closure.laser.repository;

import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.DealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DealStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealStatusRepository extends JpaRepository<DealStatus, Long> {
    Optional<DealStatus> findByName(String name);

    @Query(value = "SELECT * FROM deal_status ORDER BY sequence", nativeQuery = true)
    List<DealStatus> sortedFetch();

    @Query(value = "SELECT * FROM deal_status WHERE :key LIKE '%:value%'", nativeQuery = true)
    Page<DealStatus> search(@Param(value = "key") String key, @Param(value = "value") String value, Pageable pageable);
}
