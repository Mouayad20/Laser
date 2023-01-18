package org.closure.laser.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.DealStatus;
import org.closure.laser.repository.DealRepository;
import org.closure.laser.repository.DealStatusRepository;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DealStatus}.
 */
@Service
@Transactional
public class DealStatusService {

    private final Logger log = LoggerFactory.getLogger(DealStatusService.class);

    private static final String ENTITY_NAME = "dealStatus";

    private final DealStatusRepository dealStatusRepository;

    @Autowired
    private DealRepository dealRepository;

    public DealStatusService(DealStatusRepository dealStatusRepository) {
        this.dealStatusRepository = dealStatusRepository;
    }

    public DealStatus save(DealStatus dealStatus) {
        log.debug("Request to save DealStatus : {}", dealStatus);
        if (dealStatus.getId() != null) {
            throw new BadRequestAlertException("A new dealStatus cannot already have an ID", ENTITY_NAME, "id exists");
        }
        if (dealStatusRepository.findByName(dealStatus.getName()).isPresent()) {
            throw new BadRequestAlertException("A new dealStatus have exists name ", ENTITY_NAME, "name exists");
        }
        return dealStatusRepository.save(dealStatus);
    }

    public DealStatus update(Long id, DealStatus dealStatus) {
        log.debug("Request to save DealStatus : {}", dealStatus);
        if (dealStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, dealStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }

        if (!dealStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        if (dealStatusRepository.findByName(dealStatus.getName()).isPresent()) {
            throw new BadRequestAlertException("A new dealStatus have exists name ", ENTITY_NAME, "name exists");
        }
        return dealStatusRepository.save(dealStatus);
    }

    @Transactional(readOnly = true)
    public List<DealStatus> findAll() {
        log.debug("Request to get all DealStatuses");
        return dealStatusRepository.sortedFetch();
    }

    @Transactional(readOnly = true)
    public Optional<DealStatus> findOne(Long id) {
        log.debug("Request to get DealStatus : {}", id);
        return dealStatusRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete DealStatus : {}", id);
        if (!dealStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        dealStatusRepository.deleteById(id);
    }

    public Page<Deal> getAllDelasByStatusId(Long id, Pageable pageable) {
        if (!dealStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return dealRepository.findByStatus(dealStatusRepository.findById(id).get(), pageable);
    }
}
