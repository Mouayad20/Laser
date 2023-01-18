package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.Constants;
import org.closure.laser.repository.ConstantsRepository;
import org.closure.laser.service.ConstantsService;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.closure.laser.domain.Constants}.
 */
@RestController
@RequestMapping("/api")
public class ConstantsResource {

    private final Logger log = LoggerFactory.getLogger(ConstantsResource.class);

    private static final String ENTITY_NAME = "constants";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConstantsService constantsService;

    private final ConstantsRepository constantsRepository;

    public ConstantsResource(ConstantsService constantsService, ConstantsRepository constantsRepository) {
        this.constantsService = constantsService;
        this.constantsRepository = constantsRepository;
    }

    /**
     * {@code POST  /constants} : Create a new constants.
     *
     * @param constants the constants to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new constants, or with status {@code 400 (Bad Request)} if the constants has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/constants")
    public ResponseEntity<Constants> createConstants(@RequestBody Constants constants) throws URISyntaxException {
        log.debug("REST request to save Constants : {}", constants);
        if (constants.getId() != null) {
            throw new BadRequestAlertException("A new constants cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Constants result = constantsService.save(constants);
        return ResponseEntity
            .created(new URI("/api/constants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /constants/:id} : Updates an existing constants.
     *
     * @param id the id of the constants to save.
     * @param constants the constants to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated constants,
     * or with status {@code 400 (Bad Request)} if the constants is not valid,
     * or with status {@code 500 (Internal Server Error)} if the constants couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/constants/{id}")
    public ResponseEntity<Constants> updateConstants(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Constants constants
    ) throws URISyntaxException {
        log.debug("REST request to update Constants : {}, {}", id, constants);
        if (constants.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, constants.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!constantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Constants result = constantsService.update(constants);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, constants.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /constants/:id} : Partial updates given fields of an existing constants, field will ignore if it is null
     *
     * @param id the id of the constants to save.
     * @param constants the constants to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated constants,
     * or with status {@code 400 (Bad Request)} if the constants is not valid,
     * or with status {@code 404 (Not Found)} if the constants is not found,
     * or with status {@code 500 (Internal Server Error)} if the constants couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/constants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Constants> partialUpdateConstants(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Constants constants
    ) throws URISyntaxException {
        log.debug("REST request to partial update Constants partially : {}, {}", id, constants);
        if (constants.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, constants.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!constantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Constants> result = constantsService.partialUpdate(constants);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, constants.getId().toString())
        );
    }

    /**
     * {@code GET  /constants} : get all the constants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of constants in body.
     */
    @GetMapping("/constants")
    public List<Constants> getAllConstants() {
        log.debug("REST request to get all Constants");
        return constantsService.findAll();
    }

    /**
     * {@code GET  /constants/:id} : get the "id" constants.
     *
     * @param id the id of the constants to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the constants, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/constants/{id}")
    public ResponseEntity<Constants> getConstants(@PathVariable Long id) {
        log.debug("REST request to get Constants : {}", id);
        Optional<Constants> constants = constantsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(constants);
    }

    /**
     * {@code DELETE  /constants/:id} : delete the "id" constants.
     *
     * @param id the id of the constants to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/constants/{id}")
    public ResponseEntity<Void> deleteConstants(@PathVariable Long id) {
        log.debug("REST request to delete Constants : {}", id);
        constantsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
