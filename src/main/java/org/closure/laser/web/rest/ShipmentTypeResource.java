package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.ShipmentType;
import org.closure.laser.service.ShipmentTypeService;
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
 * REST controller for managing {@link org.closure.domain.ShipmentType}.
 */
@RestController
@RequestMapping("/api")
public class ShipmentTypeResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentTypeResource.class);

    private static final String ENTITY_NAME = "shipmentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentTypeService shipmentTypeService;

    public ShipmentTypeResource(ShipmentTypeService shipmentTypeService) {
        this.shipmentTypeService = shipmentTypeService;
    }

    /**
     * {@code POST  /shipment-types} : Create a new shipmentType.
     *
     * @param shipmentType the shipmentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new shipmentType, or with status
     *         {@code 400 (Bad Request)} if the shipmentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipment-types")
    public ResponseEntity<ShipmentType> createShipmentType(@RequestBody ShipmentType shipmentType) throws URISyntaxException {
        log.debug("REST request to save ShipmentType : {}", shipmentType);

        ShipmentType result = shipmentTypeService.save(shipmentType);
        return ResponseEntity
            .created(new URI("/api/shipment-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipment-types/:id} : Updates an existing shipmentType.
     *
     * @param id           the id of the shipmentType to save.
     * @param shipmentType the shipmentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated shipmentType,
     *         or with status {@code 400 (Bad Request)} if the shipmentType is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         shipmentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipment-types/{id}")
    public ResponseEntity<ShipmentType> updateShipmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShipmentType shipmentType
    ) throws URISyntaxException {
        log.debug("REST request to update ShipmentType : {}, {}", id, shipmentType);

        ShipmentType result = shipmentTypeService.update(id, shipmentType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shipment-types} : get all the shipmentTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of shipmentTypes in body.
     */
    @GetMapping("/shipment-types")
    public List<ShipmentType> getAllShipmentTypes() {
        log.debug("REST request to get all ShipmentTypes");
        return shipmentTypeService.findAll();
    }

    /**
     * {@code GET  /shipment-types/:id} : get the "id" shipmentType.
     *
     * @param id the id of the shipmentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the shipmentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipment-types/{id}")
    public ResponseEntity<ShipmentType> getShipmentType(@PathVariable Long id) {
        log.debug("REST request to get ShipmentType : {}", id);
        Optional<ShipmentType> shipmentType = shipmentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentType);
    }

    /**
     * {@code DELETE  /shipment-types/:id} : delete the "id" shipmentType.
     *
     * @param id the id of the shipmentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipment-types/{id}")
    public ResponseEntity<Void> deleteShipmentType(@PathVariable Long id) {
        log.debug("REST request to delete ShipmentType : {}", id);
        shipmentTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /trips/getAllShipmentsByTypeId=:id} : get page of shipments for
     * specific type .
     *
     * @param pageable the pagination information.
     * @param id       the id of type to be get shipments for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of shipments in body.
     */
    @GetMapping("/shipments-types/getAllShipmentsByTypeId={id}")
    public ResponseEntity<List<Shipment>> getAllShipmentsByTypeId(
        @PathVariable("id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of shipments by type id");
        Page<Shipment> page = shipmentTypeService.getAllShipmentsByTypeId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/shipments-types/search/{key}/{value}")
    public ResponseEntity<List<ShipmentType>> search(
        @PathVariable("key") String key,
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of shipment type");
        Page<ShipmentType> page = shipmentTypeService.search(key, value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
