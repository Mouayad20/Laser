package org.closure.laser.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Offers;
import org.closure.laser.domain.Shipment;
import org.closure.laser.repository.DealRepository;
import org.closure.laser.repository.DealStatusRepository;
import org.closure.laser.repository.OffersRepository;
import org.closure.laser.repository.ShipmentRepository;
import org.closure.laser.repository.TripRepository;
import org.closure.laser.service.dto.ShipmentDealDTO;
import org.closure.laser.service.dto.TripDealDTO;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DealService {

    private final Logger log = LoggerFactory.getLogger(DealService.class);

    private static final String ENTITY_NAME = "deal";

    private final DealRepository dealRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private DealStatusRepository dealStatusRepository;

    @Autowired
    private OffersRepository offersRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ShipmentService shipmentService;

    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public Deal save(Deal deal) {
        log.debug("Request to save Deal : {}", deal);
        if (deal.getId() != null) {
            throw new BadRequestAlertException("A new deal cannot already have an ID", ENTITY_NAME, "id exists");
        }
        return dealRepository.save(deal);
    }

    public Deal createDealLogic(Long shDealID, Long trDealID) {
        log.debug("Request to save Deal Logic shDealId : {}   ,  And tripDealId : {}", shDealID, trDealID);
        Deal shipmentDeal;
        Deal tripDeal;
        if (dealRepository.findById(shDealID).isPresent()) {
            shipmentDeal = dealRepository.findById(shDealID).get();
        } else throw new BadRequestAlertException("Shipment deal not found", ENTITY_NAME, "shipmentdealnotfound");
        if (dealRepository.findById(trDealID).isPresent()) {
            tripDeal = dealRepository.findById(trDealID).get();
        } else throw new BadRequestAlertException("Trip deal not found", ENTITY_NAME, "tripdealnotfound");
        Deal newDeal = new Deal();

        newDeal.setOwner(shipmentDeal.getOwner());
        newDeal.setDeliver(tripDeal.getDeliver());
        newDeal.setTrip(tripDeal.getTrip());
        newDeal.setStatus(dealStatusRepository.findById(3L).get()); // agreement
        double weight = 0;
        for (Shipment shipment : shipmentDeal.getShipments()) {
            weight += shipment.getWeight();
        }

        List<Deal> tripDeals = dealRepository.findByDeliverAndTrip(tripDeal.getDeliver().getId(), tripDeal.getTrip().getId());

        for (Deal deal : tripDeals) {
            if (deal.getAvailableWeight() - weight < 0) {
                throw new BadRequestAlertException(
                    "AvailableWeight = " + (deal.getAvailableWeight() - weight),
                    ENTITY_NAME,
                    "negative weight"
                );
            } else {
                deal.setAvailableWeight(deal.getAvailableWeight() - weight);
                deal.setFullWeight(deal.getFullWeight() + weight);
                dealRepository.save(deal);
            }
        }

        newDeal.setAvailableWeight(tripDeals.get(0).getAvailableWeight());
        newDeal.setFullWeight(tripDeals.get(0).getFullWeight());

        Deal savedDeal = dealRepository.save(newDeal);

        Offers offer = offersRepository.findByShipmentDealIdAndTripDealId(shDealID, trDealID).get();

        offer.setShipmentDealId(savedDeal.getId());
        offer.setStatus("Accepted");
        offersRepository.save(offer);

        for (Offers of : offersRepository.findAllByShipmentDealId(shDealID)) {
            of.setShipmentDealId(savedDeal.getId());
            of.setStatus("Closed");
            offersRepository.save(of);
        }

        delete(shipmentDeal.getId());
        List<Shipment> s = new ArrayList<>();
        for (Shipment shipment : shipmentDeal.getShipments()) {
            shipment.setDeal(savedDeal);
            s.add(shipmentService.partialUpdate(shipment.getId(), shipment).get());
        }
        savedDeal.setShipments(new HashSet<>(s));

        savedDeal.setStatus(dealStatusRepository.findById(1L).get());

        return dealRepository.save(savedDeal);
    }

    public Deal removeShipment(Long shipmentId, Long trDealID) {
        log.debug("Request to save Deal Logic : {}");

        Deal tripDeal = dealRepository.getById(trDealID);

        Shipment shipment = shipmentRepository.findById(shipmentId).get();
        shipment.setDeal(null);
        shipmentRepository.save(shipment);

        tripDeal.getShipments().remove(shipment);

        return dealRepository.save(tripDeal);
    }

    public Deal update(Long id, Deal deal) {
        log.debug("Request to save Deal : {}", deal);
        if (deal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, deal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }

        if (!dealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return dealRepository.save(deal);
    }

    public Optional<Deal> partialUpdate(Long id, Deal deal) {
        log.debug("Request to partially update Deal : {}", deal);
        if (deal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, deal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }

        return dealRepository
            .findById(deal.getId())
            .map(existingDeal -> {
                if (deal.getTotalPrice() != null) {
                    existingDeal.setTotalPrice(deal.getTotalPrice());
                }
                if (deal.getIsCashed() != null) {
                    existingDeal.setIsCashed(deal.getIsCashed());
                }
                if (deal.getFromAccount() != null) {
                    existingDeal.setFromAccount(deal.getFromAccount());
                }
                if (deal.getToAccount() != null) {
                    existingDeal.setToAccount(deal.getToAccount());
                }
                if (deal.getFullWeight() != null) {
                    existingDeal.setFullWeight(deal.getFullWeight());
                }
                if (deal.getAvailableWeight() != null) {
                    existingDeal.setAvailableWeight(deal.getAvailableWeight());
                }

                return existingDeal;
            })
            .map(dealRepository::save);
    }

    @Transactional(readOnly = true)
    public Page<Deal> findAll(Pageable pageable) {
        log.debug("Request to get all Deals");
        return dealRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Deal> findOne(Long id) {
        log.debug("Request to get Deal : {}", id);
        return dealRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Deal : {}", id);
        if (!dealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        Deal deal = dealRepository.findById(id).get();

        for (Shipment shipment : deal.getShipments()) {
            shipment.setDeal(null);
        }

        if (deal.getOwner() != null) {
            deal.getOwner().getShipmentDeals().remove(deal);
            deal.setOwner(null);
        }
        if (deal.getDeliver() != null) {
            deal.getDeliver().getShipmentDeals().remove(deal);
            deal.setDeliver(null);
        }
        deal.setTrip(null);
        deal.setDeliver(null);
        dealRepository.save(deal);
        dealRepository.deleteById(id);
    }

    public Page<Shipment> getAllShipmentsByDealId(Long id, Pageable pageable) {
        if (!dealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return shipmentRepository.findByDeal(dealRepository.findById(id).get(), pageable);
    }

    public List<Shipment> getAllShipmentsByDealId(Long id) {
        if (!dealRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return shipmentRepository.findByDeal(dealRepository.findById(id).get());
    }

    public Page<ShipmentDealDTO> findLast10DaysShipmentsDeal(Pageable pageable) {
        List<Deal> deals = dealRepository.findLast10DaysShipments(pageable).toList();

        List<ShipmentDealDTO> list = new ArrayList<>();

        for (Deal deal : deals) {
            list.add(new ShipmentDealDTO(deal, getAllShipmentsByDealId(deal.getId()), deal.getOwner()));
        }

        return new PageImpl<>(list);
    }

    public Page<TripDealDTO> findLast10DaysTripsDeal(Pageable pageable) {
        List<Deal> deals = dealRepository.findLast10DaysTrips(pageable);

        List<TripDealDTO> list = new ArrayList<>();

        for (Deal deal : deals) {
            list.add(new TripDealDTO(deal, tripRepository.findById(deal.getTrip().getId()).get(), deal.getDeliver()));
        }

        return new PageImpl<>(list);
    }

    public Deal updateStatus(Long deal_id, Long status_id) {
        Deal deal = dealRepository.findById(deal_id).get();
        deal.setStatus(dealStatusRepository.findById(status_id).get());
        deal = dealRepository.save(deal);
        return deal;
    }

    @Transactional(readOnly = true)
    public Page<Deal> search(String value, Pageable pageable) {
        log.debug("Request to get all deal by key");
        return dealRepository.search(value, pageable);
    }

    @Transactional(readOnly = true)
    public Page<TripDealDTO> searchTrips(String from, String to, Double weight, Date date, Pageable pageable) {
        List<Deal> deals = dealRepository.searchTrips(from, to, weight, date, pageable);

        log.info(">>>>>> size  : " + deals.size());

        List<TripDealDTO> list = new ArrayList<>();

        for (Deal deal : deals) {
            log.info(">>>>>  " + deal.getArrivelDate()); //
            list.add(new TripDealDTO(deal, tripRepository.findById(deal.getTrip().getId()).get(), deal.getDeliver()));
        }

        return new PageImpl<>(list);
    }

    @Transactional(readOnly = true)
    public Page<ShipmentDealDTO> searchShipments(String from, String to, Double weight, Date date, Pageable pageable) {
        List<Deal> deals = dealRepository.searchShipments(from, to, weight, date, pageable);

        List<ShipmentDealDTO> list = new ArrayList<>();

        for (Deal deal : deals) {
            list.add(new ShipmentDealDTO(deal, getAllShipmentsByDealId(deal.getId()), deal.getOwner()));
        }

        return new PageImpl<>(list);
    }
}
