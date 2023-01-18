package org.closure.laser.repository;

import java.util.Optional;
import org.closure.laser.domain.AccountProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccountProvider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountProviderRepository extends JpaRepository<AccountProvider, Long> {
    Optional<AccountProvider> findByName(String name);

    @Query(value = "SELECT * FROM account_provider WHERE :key LIKE '%:value%'", nativeQuery = true)
    Page<AccountProvider> search(@Param(value = "key") String key, @Param(value = "value") String value, Pageable pageable);
}
