package org.closure.laser.repository;

import org.closure.laser.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "SELECT * FROM article WHERE title LIKE %:value% OR content LIKE %:value% ", nativeQuery = true)
    Page<Article> search(@Param(value = "value") String value, Pageable pageable);
}
