package org.closure.laser.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.ShipmentType;
import org.closure.laser.repository.ShipmentRepository;
import org.closure.laser.repository.ShipmentTypeRepository;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShipmentType}.
 */
@Service
@Transactional
public class ShipmentTypeService {

    private final Logger log = LoggerFactory.getLogger(ShipmentTypeService.class);

    private static final String ENTITY_NAME = "shipmentType";

    private final ShipmentTypeRepository shipmentTypeRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    public ShipmentTypeService(ShipmentTypeRepository shipmentTypeRepository) {
        this.shipmentTypeRepository = shipmentTypeRepository;
    }

    public ShipmentType save(ShipmentType shipmentType) {
        log.debug("Request to save ShipmentType : {}", shipmentType);
        if (shipmentType.getId() != null) {
            throw new BadRequestAlertException("A new shipmentType cannot already have an ID", ENTITY_NAME, "id exists");
        }
        if (shipmentTypeRepository.findByName(shipmentType.getName()).isPresent()) {
            throw new BadRequestAlertException("A new shipmentType  have an exists name ", ENTITY_NAME, "name exists");
        }
        return shipmentTypeRepository.save(shipmentType);
    }

    public ShipmentType update(Long id, ShipmentType shipmentType) {
        log.debug("Request to save ShipmentType : {}", shipmentType);
        if (shipmentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, shipmentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }
        if (!shipmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id notfound");
        }
        if (shipmentTypeRepository.findByName(shipmentType.getName()).isPresent()) {
            throw new BadRequestAlertException("A new shipmentType  have an exists name ", ENTITY_NAME, "name exists");
        }
        return shipmentTypeRepository.save(shipmentType);
    }

    @Transactional(readOnly = true)
    public List<ShipmentType> findAll() {
        log.debug("Request to get all ShipmentTypes");
        return shipmentTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ShipmentType> findOne(Long id) {
        log.debug("Request to get ShipmentType : {}", id);
        return shipmentTypeRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete ShipmentType : {}", id);
        if (!shipmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        shipmentTypeRepository.deleteById(id);
    }

    public Page<Shipment> getAllShipmentsByTypeId(Long id, Pageable pageable) {
        if (!shipmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return shipmentRepository.findByType(shipmentTypeRepository.findById(id).get(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<ShipmentType> search(String key, String value, Pageable pageable) {
        log.debug("Request to get all shipment type by key");
        return shipmentTypeRepository.search(key, value, pageable);
    }
}
