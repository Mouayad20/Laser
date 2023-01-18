package org.closure.laser.web.rest;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Shipment;
import org.closure.laser.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.closure.domain.Shipment}.
 */
@RestController
@RequestMapping("/api")
public class ShipmentResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentResource.class);

    private static final String ENTITY_NAME = "shipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentService shipmentService;

    public ShipmentResource(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    /**
     * {@code POST  /shipments} : Create a new shipment.
     *
     * @param shipment the shipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new shipment, or with status {@code 400 (Bad Request)} if
     * the shipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipments")
    public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment) throws URISyntaxException {
        log.debug("REST request to save Shipment : {}", shipment);

        Shipment result = shipmentService.save(shipment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/shipments/createShipmentLogic/{date}")
    public ResponseEntity<List<Shipment>> createShipmentLogic(
        @RequestBody List<Shipment> shipments,
        @RequestHeader("Authorization") String token,
        @PathVariable(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date
    ) throws URISyntaxException {
        log.debug("REST request to save Shipment : {}", shipments);

        List<Shipment> result = shipmentService.saveLogic(shipments, token.substring("Bearer ".length()), date);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PUT  /shipments/:id} : Updates an existing shipment.
     *
     * @param id       the id of the shipment to save.
     * @param shipment the shipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated shipment,
     * or with status {@code 400 (Bad Request)} if the shipment is not
     * valid,
     * or with status {@code 500 (Internal Server Error)} if the shipment
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipments/{id}")
    public ResponseEntity<Shipment> updateShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Shipment shipment
    ) throws URISyntaxException {
        log.debug("REST request to update Shipment : {}, {}", id, shipment);

        Shipment result = shipmentService.update(id, shipment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipment.getId().toString()))
            .body(result);
    }

    //    @PutMapping("/shipments/updateDate={date}")
    //    public ResponseEntity<List<Shipment>> updateShipments(
    //        @PathVariable(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date,
    //        @RequestBody List<Shipment> shipments
    //    ) {
    //        log.debug("REST request to update Shipments : {}", shipments);
    //
    //        List<Shipment> result = shipmentService.updateShipments(shipments, date);
    //        return ResponseEntity
    //            .ok()
    //            .body(result);
    //    }

    /**
     * {@code PATCH  /shipments/:id} : Partial updates given fields of an existing
     * shipment, field will ignore if it is null
     *
     * @param id       the id of the shipment to save.
     * @param shipment the shipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated shipment,
     * or with status {@code 400 (Bad Request)} if the shipment is not
     * valid,
     * or with status {@code 404 (Not Found)} if the shipment is not
     * found,
     * or with status {@code 500 (Internal Server Error)} if the shipment
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shipments/{id}/{date}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Shipment> partialUpdateShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @PathVariable(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date,
        @RequestBody Shipment shipment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Shipment partially : {}, {}", id, shipment);

        Optional<Shipment> result = shipmentService.partialUpdate(id, shipment, date);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipment.getId().toString())
        );
    }

    /**
     * {@code GET  /shipments} : get all the shipments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of shipments in body.
     */
    @GetMapping("/shipments")
    public ResponseEntity<List<Shipment>> getAllShipments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Shipments");
        Page<Shipment> page = shipmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipments/:id} : get the "id" shipment.
     *
     * @param id the id of the shipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the shipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipments/{id}")
    public ResponseEntity<Shipment> getShipment(@PathVariable Long id) {
        log.debug("REST request to get Shipment : {}", id);
        Optional<Shipment> shipment = shipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipment);
    }

    /**
     * {@code DELETE  /shipments/:id} : delete the "id" shipment.
     *
     * @param id the id of the shipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipments/{id}")
    public ResponseEntity<Void> deleteShipment(@PathVariable Long id) {
        log.debug("REST request to delete Shipment : {}", id);
        shipmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/shipments/search/{key}/{value}")
    public ResponseEntity<List<Shipment>> search(
        @PathVariable("key") String key,
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of shipment");
        Page<Shipment> page = shipmentService.search(key, value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/shipments/searchFrom/{value}")
    public ResponseEntity<List<Shipment>> searchFrom(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to searchFrom a page of shipment");
        Page<Shipment> page = shipmentService.searchFrom(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/shipments/searchTo/{value}")
    public ResponseEntity<List<Shipment>> searchTo(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to searchTo a page of shipment");
        Page<Shipment> page = shipmentService.searchTo(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
