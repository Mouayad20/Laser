package org.closure.laser.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Location;
import org.closure.laser.domain.Trip;
import org.closure.laser.domain.UserApplication;
import org.closure.laser.repository.DealRepository;
import org.closure.laser.repository.DealStatusRepository;
import org.closure.laser.repository.LocationRepository;
import org.closure.laser.repository.TripRepository;
import org.closure.laser.repository.UserApplicationRepository;
import org.closure.laser.repository.UserRepository;
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
 * Service Implementation for managing {@link Trip}.
 */
@Service
@Transactional
public class TripService {

    private final Logger log = LoggerFactory.getLogger(TripService.class);
    private static final String ENTITY_NAME = "trip";

    private final TripRepository tripRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private DealStatusRepository dealStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip save(Trip trip) throws BadRequestAlertException {
        log.debug("Request to save Trip : {}", trip);
        if (trip.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "id exists");
        }
        if (tripRepository.findByTripIdentifier(trip.getTripIdentifier()).isPresent()) {
            throw new BadRequestAlertException("A new trip cannot  have this identifier", ENTITY_NAME, "identifier exists");
        }
        return tripRepository.save(trip);
    }

    public Trip saveLogic(Trip trip, String token, Double availableWeight, Double fullWeight) {
        log.debug("Request to save Trip : {}", trip);
        if (trip.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (trip.getTicketImage() == null) {
            throw new BadRequestAlertException("You must enter ticket image", ENTITY_NAME, "image not sent");
        }

        /* 1 - get user (deliver) from token */

        UserApplication deliver = userApplicationRepository
            .findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get())
            .get();

        /* 2 - save trip */

        Optional<Trip> optional = tripRepository.findByTripIdentifier(trip.getTripIdentifier());

        Trip savedTrip = new Trip();

        if (
            optional.isPresent() &&
            optional.get().getArriveTime().getTime() == trip.getArriveTime().getTime() &&
            optional.get().getFlyTime().getTime() == trip.getFlyTime().getTime() &&
            optional.get().getFrom().equals(trip.getFrom()) &&
            optional.get().getTo().equals(trip.getTo())
        ) {
            /* 3 - create deal for this trip and make user deliver */

            Deal deal = new Deal();
            deal.setTrip(optional.get());
            deal.setDeliver(deliver);
            deal.setStatus(dealStatusRepository.findById(1L).get()); // Waiting shipments
            deal.setFullWeight(fullWeight);
            deal.setAvailableWeight(availableWeight);
            deal.setArrivelDate(optional.get().getArriveTime());

            Deal savedDeal = dealRepository.save(deal);

            /* 4 - save trip after add deal to it */

            optional.get().getDeals().add(savedDeal);

            optional.get().setCreatedAt(new Date());
            tripRepository.save(optional.get());

            savedTrip = optional.get();
        } else {
            trip.setCreatedAt(new Date());
            savedTrip = tripRepository.save(trip);

            /* 3 - create deal for this trip and make user deliver */

            Deal deal = new Deal();
            deal.setTrip(trip);
            deal.setDeliver(deliver);
            deal.setStatus(dealStatusRepository.findById(1L).get()); // Waiting shipments
            deal.setFullWeight(fullWeight);
            deal.setAvailableWeight(availableWeight);
            deal.setArrivelDate(savedTrip.getArriveTime());

            Deal savedDeal = dealRepository.save(deal);

            /* 4 - save trip after add deal to it */

            savedTrip.getDeals().add(savedDeal);

            savedTrip = tripRepository.save(savedTrip);

            /* 5 - save this trip in from, to locations */

            Location from = locationRepository.findById(trip.getFrom().getId()).get();
            Location to = locationRepository.findById(trip.getTo().getId()).get();

            from.getTripSources().add(savedTrip);
            to.getTripDestinations().add(savedTrip);

            locationRepository.save(from);
            locationRepository.save(to);

            /* 6 - add deal to trip deals in user and save user */

            deliver.getTripsDeals().add(savedDeal);

            userApplicationRepository.save(deliver);
        }

        return savedTrip;
    }

    public Trip update(Long id, Trip trip) {
        log.debug("Request to save Trip : {}", trip);
        if (trip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, trip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }
        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        if (tripRepository.findByTripIdentifier(trip.getTripIdentifier()).isPresent()) {
            throw new BadRequestAlertException("A new trip cannot  have this identifier", ENTITY_NAME, "identifier exists");
        }
        return tripRepository.save(trip);
    }

    public Optional<Trip> partialUpdate(Long id, Trip trip, Double availableWeight, Double fullWeight) {
        log.debug("Request to partially update Trip : {}", trip);
        if (trip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (trip.getDeals().size() != 1) {
            throw new BadRequestAlertException("Entity can not update", ENTITY_NAME, "cannotupdate");
        }

        Trip edited = tripRepository.findById(id).get();
        if (trip.getFlyTime() != null) edited.setFlyTime(trip.getFlyTime());
        if (trip.getArriveTime() != null) edited.setArriveTime(trip.getArriveTime());
        if (trip.getTripIdentifier() != null) edited.setTripIdentifier(trip.getTripIdentifier());
        if (trip.getDetails() != null) edited.setDetails(trip.getDetails());
        if (trip.getTo() != null) edited.setTo(trip.getTo());
        if (trip.getFrom() != null) edited.setFrom(trip.getFrom());
        if (trip.getTicketImage() != null) edited.setTicketImage(trip.getTicketImage());

        Deal deal = dealRepository.findById(List.copyOf(trip.getDeals()).get(0).getId()).get();
        deal.setAvailableWeight(availableWeight);
        deal.setFullWeight(fullWeight);
        dealRepository.save(deal);

        return tripRepository.findById(tripRepository.save(edited).getId());
    }

    @Transactional(readOnly = true)
    public Page<Trip> findAll(Pageable pageable) {
        log.debug("Request to get all Trips");
        return tripRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Trip> findOne(Long id) {
        log.debug("Request to get Trip : {}", id);
        return tripRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Trip : {}", id);
        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        tripRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Trip> search(String value, Pageable pageable) {
        log.debug("Request to get all trips by value");
        return tripRepository.search(value, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Trip> searchFrom(String value, Pageable pageable) {
        log.debug("Request to get all trips by from");
        return tripRepository.searchFrom(value, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Trip> searchTo(String value, Pageable pageable) {
        log.debug("Request to get all trips by to");
        return tripRepository.searchTo(value, pageable);
    }

    public Page<Deal> getAllDealsByTripId(Long id, Pageable pageable) {
        if (!tripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return dealRepository.findByTrip(tripRepository.findById(id).get(), pageable);
    }
}
