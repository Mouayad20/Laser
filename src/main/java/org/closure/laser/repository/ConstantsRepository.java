package org.closure.laser.repository;

import org.closure.laser.domain.Constants;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Constants entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConstantsRepository extends JpaRepository<Constants, Long> {}
