package org.closure.laser.repository;

import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.User;
import org.closure.laser.domain.UserApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserApplication entity.
 */
@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplication, Long> {
    Optional<UserApplication> findByUser(User user);

    default Optional<UserApplication> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserApplication> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserApplication> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct userApplication from UserApplication userApplication left join fetch userApplication.user",
        countQuery = "select count(distinct userApplication) from UserApplication userApplication"
    )
    Page<UserApplication> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct userApplication from UserApplication userApplication left join fetch userApplication.user")
    List<UserApplication> findAllWithToOneRelationships();

    @Query("select userApplication from UserApplication userApplication left join fetch userApplication.user where userApplication.id =:id")
    Optional<UserApplication> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = "SELECT * FROM user_application " +
        "WHERE phone LIKE %:value% " +
        "OR passport LIKE %:value% " +
        "OR user_id IN ( SELECT id FROM jhi_user " +
        "                WHERE first_name LIKE %:value% " +
        "                OR    last_name  LIKE %:value% " +
        "                OR    email      LIKE %:value% " +
        "                OR    login      LIKE %:value% " +
        "               )",
        nativeQuery = true
    )
    Page<UserApplication> search(@Param(value = "value") String value, Pageable pageable);
}
