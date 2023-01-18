package org.closure.laser.service;

import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.Article;
import org.closure.laser.repository.ArticleRepository;
import org.closure.laser.service.ArticleService;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleService {

    private static final String ENTITY_NAME = "article";
    private final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(Article article) throws BadRequestAlertException {
        log.debug("Request to save Article : {}", article);
        if (article.getId() != null) {
            throw new BadRequestAlertException("A new article cannot already have an ID", ENTITY_NAME, "id exists");
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, Article article) throws BadRequestAlertException {
        log.debug("Request to save Article : {}", article);
        if (article.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, article.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }

        if (!articleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id notfound");
        }
        return articleRepository.save(article);
    }

    public Article partialUpdate(Long id, Article article) throws BadRequestAlertException {
        log.debug("Request to partially update Article : {}", article);
        if (article.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, article.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }

        if (!articleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return articleRepository.save(article);
        // return articleRepository
        // .findById(article.getId())
        // .map(existingArticle -> {
        // return existingArticle;
        // })
        // .map(articleRepository::save);
    }

    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Article> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id);
    }

    public void delete(Long id) throws BadRequestAlertException {
        log.debug("Request to delete Article : {}", id);
        if (!articleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        articleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Article> search(String value, Pageable pageable) {
        log.debug("Request to get all article by value");
        return articleRepository.search(value, pageable);
    }

    public void addView(Long id) {
        Article article = articleRepository.findById(id).get();
        article.setViewcount(article.getViewcount() + 1);
        articleRepository.save(article);
    }
}
