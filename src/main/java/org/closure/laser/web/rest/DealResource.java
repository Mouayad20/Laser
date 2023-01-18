package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Shipment;
import org.closure.laser.service.DealService;
import org.closure.laser.service.dto.ShipmentDealDTO;
import org.closure.laser.service.dto.TripDealDTO;
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
 * REST controller for managing {@link org.closure.domain.Deal}.
 */
@RestController
@RequestMapping("/api")
public class DealResource {

    private static final String ENTITY_NAME = "deal";
    private final Logger log = LoggerFactory.getLogger(DealResource.class);
    private final DealService dealService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public DealResource(DealService dealService) {
        this.dealService = dealService;
    }

    /**
     * {@code POST  /deals} : Create a new deal.
     *
     * @param deal the deal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new deal, or with status {@code 400 (Bad Request)} if the
     *         deal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deals")
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) throws URISyntaxException {
        log.debug("REST request to save Deal : {}", deal);

        Deal result = dealService.save(deal);
        return ResponseEntity
            .created(new URI("/api/deals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/deals/dealLogic/{shDealId}/{trDealId}")
    public ResponseEntity<Deal> createDealLogic(
        @PathVariable(name = "shDealId") Long shDealID,
        @PathVariable(name = "trDealId") Long trDealID
    ) throws URISyntaxException {
        log.debug("REST request to save Deal Logic : ");

        Deal result = dealService.createDealLogic(shDealID, trDealID);
        return ResponseEntity
            .created(new URI("/api/deals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/deals/removeShipment/{shipmentId}/{trDealId}")
    public ResponseEntity<Deal> removeShipment(
        @PathVariable(name = "shipmentId") Long shipmentId,
        @PathVariable(name = "trDealId") Long trDealID
    ) throws URISyntaxException {
        log.debug("REST request to save Deal Logic : ");

        Deal result = dealService.removeShipment(shipmentId, trDealID);
        return ResponseEntity
            .created(new URI("/api/deals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deals/:id} : Updates an existing deal.
     *
     * @param id   the id of the deal to save.
     * @param deal the deal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated deal,
     *         or with status {@code 400 (Bad Request)} if the deal is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the deal
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deals/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable(value = "id", required = false) final Long id, @RequestBody Deal deal)
        throws URISyntaxException {
        log.debug("REST request to update Deal : {}, {}", id, deal);

        Deal result = dealService.update(id, deal);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deal.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deals/:id} : Partial updates given fields of an existing deal,
     * field will ignore if it is null
     *
     * @param id   the id of the deal to save.
     * @param deal the deal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated deal,
     *         or with status {@code 400 (Bad Request)} if the deal is not valid,
     *         or with status {@code 404 (Not Found)} if the deal is not found,
     *         or with status {@code 500 (Internal Server Error)} if the deal
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Deal> partialUpdateDeal(@PathVariable(value = "id", required = false) final Long id, @RequestBody Deal deal)
        throws URISyntaxException {
        log.debug("REST request to partial update Deal partially : {}, {}", id, deal);

        Optional<Deal> result = dealService.partialUpdate(id, deal);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deal.getId().toString())
        );
    }

    /**
     * {@code GET  /deals} : get all the deals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of deals in body.
     */
    @GetMapping("/deals")
    public ResponseEntity<List<Deal>> getAllDeals(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Deals");
        Page<Deal> page = dealService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deals/:id} : get the "id" deal.
     *
     * @param id the id of the deal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the deal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deals/{id}")
    public ResponseEntity<Deal> getDeal(@PathVariable Long id) {
        log.debug("REST request to get Deal : {}", id);
        Optional<Deal> deal = dealService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deal);
    }

    /**
     * {@code DELETE  /deals/:id} : delete the "id" deal.
     *
     * @param id the id of the deal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deals/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable Long id) {
        log.debug("REST request to delete Deal : {}", id);
        dealService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /trips/getAllShipmentsByDealId=:id} : get page of shipments for
     * specific deal .
     *
     * @param pageable the pagination information.
     * @param id       the id of deal to be get shipments for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of shipments in body.
     */
    @GetMapping("/deals/getAllShipmentsByDealId={id}")
    public ResponseEntity<List<Shipment>> getAllShipmentsByDealId(
        @PathVariable("id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of shipments by type id");
        Page<Shipment> page = dealService.getAllShipmentsByDealId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/deals/newShipments")
    public ResponseEntity<List<ShipmentDealDTO>> findLast10DaysShipmentsDeal(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of last 10 days shipment deals ");
        Page<ShipmentDealDTO> page = dealService.findLast10DaysShipmentsDeal(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/deals/newTrips")
    public ResponseEntity<List<TripDealDTO>> findLast10DaysTripsDeal(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of last 10 days trip deals ");
        Page<TripDealDTO> page = dealService.findLast10DaysTripsDeal(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/deals/updateStatus/{deal_id}/{status_id}")
    public ResponseEntity<Deal> updateStatus(
        @PathVariable(name = "deal_id") Long deal_id,
        @PathVariable(name = "status_id") Long status_id
    ) {
        log.debug("REST request to update Status of deal ");
        Deal deal = dealService.updateStatus(deal_id, status_id);
        return ResponseEntity.ok().body(deal);
    }

    @GetMapping("/deals/search/{value}")
    public ResponseEntity<List<Deal>> search(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of deal");
        Page<Deal> page = dealService.search(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /* */
    @GetMapping("/deals/searchTrips/{from}/{to}/{weight}/{date}") //
    public ResponseEntity<List<TripDealDTO>> searchTrips(
        @PathVariable(name = "from") String from,
        @PathVariable(name = "to") String to,
        @PathVariable(name = "weight") Double weight,
        @PathVariable(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<TripDealDTO> page = dealService.searchTrips(from, to, weight, date, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/deals/searchShipments/{from}/{to}/{weight}/{date}")
    public ResponseEntity<List<ShipmentDealDTO>> searchShipments(
        @PathVariable(name = "from") String from,
        @PathVariable(name = "to") String to,
        @PathVariable(name = "weight") Double weight,
        @PathVariable(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<ShipmentDealDTO> page = dealService.searchShipments(from, to, weight, date, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
