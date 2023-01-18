package org.closure.laser.repository;

import org.closure.laser.domain.AccountProvider;
import org.closure.laser.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByProvider(AccountProvider provider, Pageable pageable);

    @Query(value = "SELECT * FROM transation WHERE :key LIKE '%:value%'", nativeQuery = true)
    Page<Transaction> search(@Param(value = "key") String key, @Param(value = "value") String value, Pageable pageable);
}
