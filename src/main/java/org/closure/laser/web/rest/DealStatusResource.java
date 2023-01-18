package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.DealStatus;
import org.closure.laser.service.DealStatusService;
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
 * REST controller for managing {@link org.closure.domain.DealStatus}.
 */
@RestController
@RequestMapping("/api")
public class DealStatusResource {

    private final Logger log = LoggerFactory.getLogger(DealStatusResource.class);

    private static final String ENTITY_NAME = "dealStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DealStatusService dealStatusService;

    public DealStatusResource(DealStatusService dealStatusService) {
        this.dealStatusService = dealStatusService;
    }

    /**
     * {@code POST  /deal-statuses} : Create a new dealStatus.
     *
     * @param dealStatus the dealStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new dealStatus, or with status {@code 400 (Bad Request)}
     *         if the dealStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deal-statuses")
    public ResponseEntity<DealStatus> createDealStatus(@RequestBody DealStatus dealStatus) throws URISyntaxException {
        log.debug("REST request to save DealStatus : {}", dealStatus);
        DealStatus result = dealStatusService.save(dealStatus);
        return ResponseEntity
            .created(new URI("/api/deal-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deal-statuses/:id} : Updates an existing dealStatus.
     *
     * @param id         the id of the dealStatus to save.
     * @param dealStatus the dealStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated dealStatus,
     *         or with status {@code 400 (Bad Request)} if the dealStatus is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         dealStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deal-statuses/{id}")
    public ResponseEntity<DealStatus> updateDealStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DealStatus dealStatus
    ) throws URISyntaxException {
        log.debug("REST request to update DealStatus : {}, {}", id, dealStatus);

        DealStatus result = dealStatusService.update(id, dealStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dealStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /deal-statuses} : get all the dealStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of dealStatuses in body.
     */
    @GetMapping("/deal-statuses")
    public List<DealStatus> getAllDealStatuses() {
        log.debug("REST request to get all DealStatuses");
        return dealStatusService.findAll();
    }

    /**
     * {@code GET  /deal-statuses/:id} : get the "id" dealStatus.
     *
     * @param id the id of the dealStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the dealStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deal-statuses/{id}")
    public ResponseEntity<DealStatus> getDealStatus(@PathVariable Long id) {
        log.debug("REST request to get DealStatus : {}", id);
        Optional<DealStatus> dealStatus = dealStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dealStatus);
    }

    /**
     * {@code DELETE  /deal-statuses/:id} : delete the "id" dealStatus.
     *
     * @param id the id of the dealStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deal-statuses/{id}")
    public ResponseEntity<Void> deleteDealStatus(@PathVariable Long id) {
        log.debug("REST request to delete DealStatus : {}", id);
        dealStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /deal-statuses/getAllDelasByStatusId=:id} : get page of deals for
     * specific status .
     *
     * @param pageable the pagination information.
     * @param id       the id of status to be get deals for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of deals in body.
     */
    @GetMapping("/deal-statuses/getAllDelasByStatusId={id}")
    public ResponseEntity<List<Deal>> getAllDelasByStatusId(
        @PathVariable("id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of deals by satuts id");
        Page<Deal> page = dealStatusService.getAllDelasByStatusId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
