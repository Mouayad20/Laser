package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Location;
import org.closure.laser.service.LocationService;
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
 * REST controller for managing {@link org.closure.domain.Location}.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "location";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationService locationService;

    public LocationResource(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * {@code POST  /locations} : Create a new location.
     *
     * @param location the location to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new location, or with status {@code 400 (Bad Request)} if
     *         the location has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);

        Location result = locationService.save(location);
        return ResponseEntity
            .created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locations/:id} : Updates an existing location.
     *
     * @param id       the id of the location to save.
     * @param location the location to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated location,
     *         or with status {@code 400 (Bad Request)} if the location is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the location
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locations/{id}")
    public ResponseEntity<Location> updateLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Location location
    ) throws URISyntaxException {
        log.debug("REST request to update Location : {}, {}", id, location);

        Location result = locationService.update(id, location);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, location.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /locations/:id} : Partial updates given fields of an existing
     * location, field will ignore if it is null
     *
     * @param id       the id of the location to save.
     * @param location the location to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated location,
     *         or with status {@code 400 (Bad Request)} if the location is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the location is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the location
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/locations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Location> partialUpdateLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Location location
    ) throws URISyntaxException {
        log.debug("REST request to partial update Location partially : {}, {}", id, location);

        Optional<Location> result = locationService.partialUpdate(id, location);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, location.getId().toString())
        );
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of locations in body.
     */
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Locations");
        Page<Location> page = locationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locations/:id} : get the "id" location.
     *
     * @param id the id of the location to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the location, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        Optional<Location> location = locationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(location);
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" location.
     *
     * @param id the id of the location to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/locations/search/{key}/{value}")
    public ResponseEntity<List<Location>> search(
        @PathVariable("key") String key,
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of location");
        Page<Location> page = locationService.search(key, value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/locations/fly")
    public List<Location> fly() {
        return locationService.getFromAPI();
    }
}
