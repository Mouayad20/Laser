package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Article;
import org.closure.laser.service.ArticleService;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.closure.domain.Article}.
 */
@RestController
@RequestMapping("/api")
public class ArticleResource {

    private static final String ENTITY_NAME = "article";
    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);
    private final ArticleService articleService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ArticleResource(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * {@code POST  /articles} : Create a new article.
     *
     * @param articleDTO the articleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new articleDTO, or with status {@code 400 (Bad Request)} if
     *         the article has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/articles")
    public ResponseEntity<Object> createArticle(@RequestBody Article articleDTO) {
        log.debug("REST request to save Article : {}", articleDTO);
        try {
            Article result = articleService.save(articleDTO);
            return ResponseEntity
                .created(new URI("/api/articles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (BadRequestAlertException | URISyntaxException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * {@code PUT  /articles/:id} : Updates an existing article.
     *
     * @param id         the id of the articleDTO to save.
     * @param articleDTO the articleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated articleDTO,
     *         or with status {@code 400 (Bad Request)} if the articleDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the articleDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/articles/{id}")
    public ResponseEntity<Object> updateArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Article articleDTO
    ) {
        log.debug("REST request to update Article : {}, {}", id, articleDTO);
        try {
            Article result = articleService.update(id, articleDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleDTO.getId().toString()))
                .body(result);
        } catch (BadRequestAlertException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * {@code PATCH  /articles/:id} : Partial updates given fields of an existing
     * article, field will ignore if it is null
     *
     * @param id         the id of the articleDTO to save.
     * @param articleDTO the articleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated articleDTO,
     *         or with status {@code 400 (Bad Request)} if the articleDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the articleDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the articleDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Object> partialUpdateArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Article articleDTO
    ) {
        log.debug("REST request to partial update Article partially : {}, {}", id, articleDTO);
        try {
            Article result = articleService.partialUpdate(id, articleDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, articleDTO.getId().toString()))
                .body(result);
        } catch (BadRequestAlertException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    /**
     * {@code GET  /articles} : get all the articles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of articles in body.
     */
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Articles");
        Page<Article> page = articleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /articles/:id} : get the "id" article.
     *
     * @param id the id of the articleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the articleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Optional<Article> articleDTO = articleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleDTO);
    }

    /**
     * {@code DELETE  /articles/:id} : delete the "id" article.
     *
     * @param id the id of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Object> deleteArticle(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        try {
            articleService.delete(id);
            return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
        } catch (BadRequestAlertException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/articles/search/{value}")
    public ResponseEntity<List<Article>> search(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of article");
        Page<Article> page = articleService.search(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/articles/addView/{id}")
    public void addView(@PathVariable Long id) {
        articleService.addView(id);
    }
}
