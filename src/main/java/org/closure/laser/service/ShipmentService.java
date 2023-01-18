package org.closure.laser.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Location;
import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.UserApplication;
import org.closure.laser.repository.*;
import org.closure.laser.security.jwt.TokenProvider;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private final Logger log = LoggerFactory.getLogger(ShipmentService.class);

    private static final String ENTITY_NAME = "shipment";

    private final ShipmentRepository shipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private DealStatusRepository dealStatusRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OffersRepository offersRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Shipment save(Shipment shipment) {
        log.debug("Request to save Shipment : {}", shipment);
        if (shipment.getId() != null) {
            throw new BadRequestAlertException("A new shipment cannot already have an ID", ENTITY_NAME, "id exists");
        }
        return shipmentRepository.save(shipment);
    }

    public List<Shipment> saveLogic(List<Shipment> shipments, String token, Date date) {
        log.debug("Request to save List of Shipments : ");

        /* 1 - get user (owner) from token */

        UserApplication owner = userApplicationRepository
            .findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get())
            .get();

        /* 2 - save shipment *///

        Deal deal = new Deal();
        double weights = 0;
        List<Shipment> savedShipments = new ArrayList<>();
        for (int i = 0; i < shipments.size(); i++) {
            weights += shipments.get(i).getWeight();
            shipments.get(i).setCreatedAt(new Date());
            shipments.get(i).setImgUrl("");
            savedShipments.add(shipmentRepository.save(shipments.get(i)));
            deal.getShipments().add(savedShipments.get(i));
        }

        /* 3 - create deal for this shipment and make user owner */

        deal.setOwner(owner);
        deal.setStatus(dealStatusRepository.findById(1L).get()); // Waiting shipments
        deal.setFullWeight(weights);
        deal.setAvailableWeight(weights);
        deal.setExpectedDate(date); //
        /* */
        Deal savedDeal = dealRepository.save(deal);

        /* 4 - save shipment after add deal to it */
        /* 5 - save this shipment in from, to locations */

        Location from = locationRepository.findById(shipments.get(0).getFrom().getId()).get();
        Location to = locationRepository.findById(shipments.get(0).getTo().getId()).get();

        for (int index = 0; index < savedShipments.size(); index++) {
            savedShipments.get(index).setDeal(savedDeal);
            from.getShipmentSources().add(shipmentRepository.save(savedShipments.get(index)));
            to.getShipmentDestinations().add(shipmentRepository.save(savedShipments.get(index)));
        }

        locationRepository.save(from);
        locationRepository.save(to);

        /* 6 - add deal to shipment deals in user and save user */

        owner.getShipmentDeals().add(savedDeal);

        userApplicationRepository.save(owner);

        return savedShipments;
    }

    public Shipment update(Long id, Shipment shipment) {
        log.debug("Request to save Shipment : {}", shipment);
        if (shipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, shipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }

        if (!shipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return shipmentRepository.save(shipment);
    }

    public Optional<Shipment> partialUpdate(Long id, Shipment shipment) {
        log.debug("Request to partially update Shipment : {}", shipment);
        if (shipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Shipment edited = shipmentRepository.findById(shipment.getId()).get();

        if (shipment.getWeight() != null) edited.setWeight(shipment.getWeight());
        if (shipment.getUrl() != null) edited.setUrl(shipment.getUrl());
        if (shipment.getDescription() != null) edited.setDescription(shipment.getDescription());
        if (shipment.getCost() != null) edited.setCost(shipment.getCost());
        if (shipment.getPrice() != null) edited.setPrice(shipment.getPrice());
        if (shipment.getDetails() != null) edited.setDetails(shipment.getDetails());
        if (shipment.getType() != null) edited.setType(shipment.getType());
        if (shipment.getTo() != null) edited.setTo(shipment.getTo());
        if (shipment.getFrom() != null) edited.setFrom(shipment.getFrom());
        if (shipment.getImgUrl() != null) edited.setImgUrl(shipment.getImgUrl());

        return shipmentRepository.findById(shipmentRepository.save(edited).getId());
    }

    public Optional<Shipment> partialUpdate(Long id, Shipment shipment, Date date) {
        log.debug("Request to partially update Shipment : {}", shipment);
        if (shipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (offersRepository.findByShipmentDealId(shipment.getDeal().getId()).size() > 0) {
            throw new BadRequestAlertException("This shipment is used in offer", ENTITY_NAME, "shipmentUsed");
        }

        Deal deal = dealRepository.findById(shipment.getDeal().getId()).get();
        if (date != null) {
            deal.setExpectedDate(date);
        }

        Shipment edited = shipmentRepository.findById(shipment.getId()).get();

        if (shipment.getWeight() != null) edited.setWeight(shipment.getWeight());
        if (shipment.getUrl() != null) edited.setUrl(shipment.getUrl());
        if (shipment.getDescription() != null) edited.setDescription(shipment.getDescription());
        if (shipment.getCost() != null) edited.setCost(shipment.getCost());
        if (shipment.getPrice() != null) edited.setPrice(shipment.getPrice());
        if (shipment.getDetails() != null) edited.setDetails(shipment.getDetails());
        if (shipment.getType() != null) edited.setType(shipment.getType());
        if (shipment.getTo() != null) edited.setTo(shipment.getTo());
        if (shipment.getFrom() != null) edited.setFrom(shipment.getFrom());
        if (shipment.getImgUrl() != null) edited.setImgUrl(shipment.getImgUrl());

        return shipmentRepository.findById(shipmentRepository.save(edited).getId());
    }

    @Transactional(readOnly = true)
    public Page<Shipment> findAll(Pageable pageable) {
        log.debug("Request to get all Shipments");
        return shipmentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Shipment> findOne(Long id) {
        log.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Shipment : {}", id);
        if (!shipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        shipmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Shipment> search(String key, String value, Pageable pageable) {
        log.debug("Request to get all shipment by key");
        return shipmentRepository.search(key, value, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Shipment> searchFrom(String value, Pageable pageable) {
        log.debug("Request to get all shipment by from");
        return shipmentRepository.searchFrom(value, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Shipment> searchTo(String value, Pageable pageable) {
        log.debug("Request to get all shipment by to");
        return shipmentRepository.searchTo(value, pageable);
    }

    public List<Shipment> updateShipments(List<Shipment> shipments, Date date) {
        List<Shipment> list = new ArrayList<>();

        shipments
            .stream()
            .map(shipment -> {
                if (offersRepository.findByShipmentDealId(shipment.getDeal().getId()).size() > 0) {
                    throw new BadRequestAlertException("This shipment is used in offer", ENTITY_NAME, "shipmentUsed");
                }
                list.add(shipmentRepository.save(shipment));
                return null;
            });
        Deal deal = dealRepository.findById(shipments.get(0).getDeal().getId()).get();
        deal.setExpectedDate(date);
        dealRepository.save(deal);
        return list;
    }
}
