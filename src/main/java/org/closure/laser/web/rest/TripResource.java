package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Trip;
import org.closure.laser.service.TripService;
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
 * REST controller for managing {@link org.closure.domain.Trip}.
 */
@RestController
@RequestMapping("/api")
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripService tripService;

    public TripResource(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * {@code POST  /trips} : Create a new trip.
     *
     * @param tripDTO the tripDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new tripDTO, or with status {@code 400 (Bad Request)} if the
     * trip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trips")
    public ResponseEntity<Trip> createTrip(@RequestBody Trip tripDTO) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", tripDTO);
        Trip result = tripService.save(tripDTO);
        return ResponseEntity
            .created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/trips/createTripLogic/fw={fullWeight}/avw={availableWeight}")
    public ResponseEntity<Trip> createTripLogic(
        @RequestBody Trip trip,
        @RequestHeader("Authorization") String token,
        @PathVariable("availableWeight") Double availableWeight,
        @PathVariable("fullWeight") Double fullWeight
    ) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", trip);

        Trip result = tripService.saveLogic(trip, token.substring("Bearer ".length()), availableWeight, fullWeight);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /trips/:id} : Updates an existing trip.
     *
     * @param id      the id of the tripDTO to save.
     * @param tripDTO the tripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated tripDTO,
     * or with status {@code 400 (Bad Request)} if the tripDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trips/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable(value = "id", required = false) final Long id, @RequestBody Trip tripDTO)
        throws URISyntaxException {
        log.debug("REST request to update Trip : {}, {}", id, tripDTO);
        Trip result = tripService.update(id, tripDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trips/:id} : Partial updates given fields of an existing trip,
     * field will ignore if it is null
     *
     * @param id      the id of the tripDTO to save.
     * @param tripDTO the tripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated tripDTO,
     * or with status {@code 400 (Bad Request)} if the tripDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tripDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = "/trips/update/{id}/fw={fullWeight}/avw={availableWeight}",
        consumes = { "application/json", "application/merge-patch+json" }
    )
    public ResponseEntity<Trip> partialUpdateTrip(
        @PathVariable(value = "id") final Long id,
        @PathVariable("availableWeight") Double availableWeight,
        @PathVariable("fullWeight") Double fullWeight,
        @RequestBody Trip tripDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trip partially : {}, {}", id, tripDTO);

        Optional<Trip> result = tripService.partialUpdate(id, tripDTO, availableWeight, fullWeight);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trips} : get all the trips.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of trips in body.
     */
    @GetMapping("/trips")
    public ResponseEntity<List<Trip>> getAllTrips(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Trips");
        Page<Trip> page = tripService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trips/:id} : get the "id" trip.
     *
     * @param id the id of the tripDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the tripDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trips/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<Trip> tripDTO = tripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripDTO);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trip.
     *
     * @param id the id of the tripDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trips/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        log.debug("REST request to delete Trip : {}", id);
        tripService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /trips/getAllDealsByTripId=:id} : get page of deals for specific
     * trip .
     *
     * @param pageable the pagination information.
     * @param id       the id of trip to be get deals for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of trips in body.
     */
    @GetMapping("/trips/getAllDealsByTripId={id}")
    public ResponseEntity<List<Deal>> getAllDealsByTripId(
        @PathVariable("id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of deals by trip id");
        Page<Deal> page = tripService.getAllDealsByTripId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/trips/search/{value}")
    public ResponseEntity<List<Trip>> search(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of trip");
        Page<Trip> page = tripService.search(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/trips/searchFrom/{value}")
    public ResponseEntity<List<Trip>> searchFrom(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to searchFrom a page of trip");
        Page<Trip> page = tripService.searchFrom(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/trips/searchTo/{value}")
    public ResponseEntity<List<Trip>> searchTo(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to searchTo a page of trip");
        Page<Trip> page = tripService.searchTo(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
