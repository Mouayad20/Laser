package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.Offers;
import org.closure.laser.repository.OffersRepository;
import org.closure.laser.service.OffersService;
import org.closure.laser.service.dto.OffersDTO;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.closure.laser.domain.Offers}.
 */
@RestController
@RequestMapping("/api")
public class OffersResource {

    private final Logger log = LoggerFactory.getLogger(OffersResource.class);

    private static final String ENTITY_NAME = "offers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OffersService offersService;

    private final OffersRepository offersRepository;

    public OffersResource(OffersService offersService, OffersRepository offersRepository) {
        this.offersService = offersService;
        this.offersRepository = offersRepository;
    }

    /**
     * {@code POST  /offers} : Create a new offers.
     *
     * @param offers the offers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new offers, or with status {@code 400 (Bad Request)} if the
     *         offers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/offers")
    public ResponseEntity<Offers> createOffers(@RequestBody Offers offers) throws URISyntaxException {
        log.debug("REST request to save Offers : {}", offers);
        if (offers.getId() != null) {
            throw new BadRequestAlertException("A new offers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Offers result = offersService.save(offers);
        return ResponseEntity
            .created(new URI("/api/offers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /offers/:id} : Updates an existing offers.
     *
     * @param id     the id of the offers to save.
     * @param offers the offers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated offers,
     *         or with status {@code 400 (Bad Request)} if the offers is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the offers
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/offers/{id}")
    public ResponseEntity<Offers> updateOffers(@PathVariable(value = "id", required = false) final Long id, @RequestBody Offers offers)
        throws URISyntaxException {
        log.debug("REST request to update Offers : {}, {}", id, offers);
        if (offers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Offers result = offersService.update(offers);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offers.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /offers/:id} : Partial updates given fields of an existing
     * offers, field will ignore if it is null
     *
     * @param id     the id of the offers to save.
     * @param offers the offers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated offers,
     *         or with status {@code 400 (Bad Request)} if the offers is not valid,
     *         or with status {@code 404 (Not Found)} if the offers is not found,
     *         or with status {@code 500 (Internal Server Error)} if the offers
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/offers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OffersDTO> partialUpdateOffers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Offers offers
    ) throws URISyntaxException {
        log.debug("REST request to partial update Offers partially : {}, {}", id, offers);
        if (offers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OffersDTO result = offersService.partialUpdate(offers);

        return ResponseEntity.ok(result);
    }

    /**
     * {@code GET  /offers} : get all the offers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of offers in body.
     */
    @GetMapping("/offers")
    public List<Offers> getAllOffers() {
        log.debug("REST request to get all Offers");
        return offersService.findAll();
    }

    /**
     * {@code GET  /offers/:id} : get the "id" offers.
     *
     * @param id the id of the offers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the offers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/offers/{id}")
    public ResponseEntity<OffersDTO> getOffers(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        log.debug("REST request to get Offers : {}", id);
        OffersDTO dto = offersService.findOne(id, token.substring("Bearer ".length()));
        return ResponseEntity.ok(dto);
    }

    /**
     * {@code DELETE  /offers/:id} : delete the "id" offers.
     *
     * @param id the id of the offers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deleteOffers(@PathVariable Long id) {
        log.debug("REST request to delete Offers : {}", id);
        offersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/offers/addOffers/{shD_id}/{trD_id}") /// plus header JWT
    public ResponseEntity<Offers> addOffers(
        @RequestHeader("Authorization") String token,
        @PathVariable(name = "shD_id") Long shD_id,
        @PathVariable(name = "trD_id") Long trD_id
    ) {
        log.debug("REST request to add Offer ");
        return offersService.addOffers(token.substring("Bearer ".length()), shD_id, trD_id);
    }

    @GetMapping("/offers/send")
    public String send() {
        return offersService.send();
    }

    @GetMapping("/offers/getAllForUser")
    public List<OffersDTO> getAllForUser(
        @RequestHeader("Authorization") String token,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all Offers for user");
        return offersService.getAllForUser(token.substring("Bearer ".length()), pageable);
    }
}
