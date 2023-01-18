package org.closure.laser.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Offers;
import org.closure.laser.domain.UserApplication;
import org.closure.laser.repository.DealRepository;
import org.closure.laser.repository.OffersRepository;
import org.closure.laser.repository.UserApplicationRepository;
import org.closure.laser.repository.UserRepository;
import org.closure.laser.security.jwt.TokenProvider;
import org.closure.laser.service.dto.OffersDTO;
import org.closure.laser.service.dto.ShipmentDealDTO;
import org.closure.laser.service.dto.TripDealDTO;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Offers}.
 */
@Service
@Transactional
public class OffersService {

    private final Logger log = LoggerFactory.getLogger(OffersService.class);

    private final OffersRepository offersRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    public OffersService(OffersRepository offersRepository) {
        this.offersRepository = offersRepository;
    }

    /**
     * Save a offers.
     *
     * @param offers the entity to save.
     * @return the persisted entity.
     */
    public Offers save(Offers offers) {
        log.debug("Request to save Offers : {}", offers);
        return offersRepository.save(offers);
    }

    /**
     * Partially update a offers.
     *
     * @param offers the entity to update partially.
     * @return the persisted entity.
     */
    public OffersDTO partialUpdate(Offers offers) {
        log.debug("Request to partially update Offers : {}", offers);
        Offers offer = offersRepository
            .findById(offers.getId())
            .map(existingOffers -> {
                if (offers.getShipmentDealId() != null) {
                    existingOffers.setShipmentDealId(offers.getShipmentDealId());
                }
                if (offers.getTripDealId() != null) {
                    existingOffers.setTripDealId(offers.getTripDealId());
                }
                if (offers.getStatus() != null) {
                    existingOffers.setStatus(offers.getStatus());
                }

                return existingOffers;
            })
            .map(offersRepository::save)
            .get();

        Deal shDeal = dealRepository.findById(offer.getShipmentDealId()).get();
        Deal trDeal = dealRepository.findById(offer.getTripDealId()).get();

        OffersDTO dto = new OffersDTO();
        dto.setId(offer.getId());
        dto.setStatus(offer.getStatus());
        dto.setSenderId(offer.getSenderId());
        dto.setTripDealDTO(new TripDealDTO(trDeal, trDeal.getTrip(), trDeal.getDeliver()));
        dto.setShipmentDealDTO(new ShipmentDealDTO(shDeal, List.copyOf(shDeal.getShipments()), shDeal.getOwner()));

        return dto;
    }

    /**
     * Get all the offers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Offers> findAll() {
        log.debug("Request to get all Offers");
        return offersRepository.findAll();
    }

    /**
     * Get one offers by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public OffersDTO findOne(Long id, String token) {
        log.debug("Request to get Offers : {}", id);

        Offers offer = offersRepository.findById(id).get();

        Deal shDeal = dealRepository.findById(offer.getShipmentDealId()).get();
        Deal trDeal = dealRepository.findById(offer.getTripDealId()).get();

        OffersDTO dto = new OffersDTO();
        dto.setId(offer.getId());

        dto.setStatus(offer.getStatus());
        dto.setSenderId(offer.getSenderId());

        dto.setTripDealDTO(new TripDealDTO(trDeal, trDeal.getTrip(), trDeal.getDeliver()));
        dto.setShipmentDealDTO(new ShipmentDealDTO(shDeal, List.copyOf(shDeal.getShipments()), shDeal.getOwner()));

        return dto;
    }

    /**
     * Update a offers.
     *
     * @param offers the entity to save.
     * @return the persisted entity.
     */
    public Offers update(Offers offers) {
        log.debug("Request to save Offers : {}", offers);
        return offersRepository.save(offers);
    }

    /* */

    /**
     * Delete the offers by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Offers : {}", id);
        offersRepository.deleteById(id);
    }

    public ResponseEntity<Offers> addOffers(String token, Long shD_id, Long trD_id) {
        if (!dealRepository.existsById(shD_id)) {
            throw new BadRequestAlertException("Entity not found", "Shipment Deal", "notfound");
        }
        if (!dealRepository.existsById(trD_id)) {
            throw new BadRequestAlertException("Entity not found", "Trip Deal", "notfound");
        }
        if (offersRepository.findByTripDealIdAndShipmentDealId(trD_id, shD_id).isPresent()) {
            throw new BadRequestAlertException("Offer added before", "Trip And Shipment Deal", "offerexist");
        }
        if (dealRepository.findById(shD_id).get().getFullWeight() > dealRepository.findById(trD_id).get().getAvailableWeight()) {
            throw new BadRequestAlertException("Weight of shipments greater than available weight of trip", "Shipment Deal", "Weighterror");
        }
        UserApplication userApplication = new UserApplication();
        if (
            userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).isPresent() &&
            userApplicationRepository.findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get()).isPresent()
        ) {
            userApplication =
                userApplicationRepository.findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get()).get();
        } else {
            throw new BadRequestAlertException("Entity not found", "User Application", "notfound");
        }

        Deal shDeal = dealRepository.findById(shD_id).get();
        Deal trDeal = dealRepository.findById(trD_id).get();
        Offers offer = new Offers();
        offer.setShipmentDealId(shD_id);
        offer.setTripDealId(trD_id);
        offer.setStatus("Pending");
        offer.setSenderId(userApplication.getId());
        Offers savedOffer = offersRepository.save(offer);
        if (shDeal.getOwner().getId() == userApplication.getId()) {
            /// send to delevier
            firebaseService.sendNotification(shDeal.getOwner(), trDeal.getDeliver().getConnection().getFcmToken(), savedOffer.getId());
        } else {
            /// send to owner
            firebaseService.sendNotification(trDeal.getDeliver(), shDeal.getOwner().getConnection().getFcmToken(), savedOffer.getId());
        }

        return ResponseEntity.ok().body(savedOffer);
    }

    public String send() {
        return "test";
        // return firebaseService.sendNotification(
        // "dNhBu5xQTPWfWLL0cyqbQE:APA91bG0r2EL__zwt-9jBfHruuwe_kMDqPP3UCexbWXINlyhsmJdJIUfnn2heOc74WiSGA-dDraqbiv3quJ2nO3_rtkWJyOLMvnVIJ6Q-AVtbu3zENWw_VyyLHYwQNqGjuT7wRezDr9f");
    }

    public List<OffersDTO> getAllForUser(String token, Pageable pageable) {
        UserApplication user = userApplicationRepository
            .findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get())
            .get();

        List<OffersDTO> offersDTOs = new ArrayList<>();

        List<Deal> tripDeals = dealRepository.findByDeliver(user, pageable);
        for (Deal tripDeal : tripDeals) {
            if (offersRepository.findByTripDealId(tripDeal.getId()).size() > 0) {
                offersDTOs.add(
                    new OffersDTO(
                        offersRepository.findByTripDealId(tripDeal.getId()).get(0).getId(),
                        new TripDealDTO(tripDeal, tripDeal.getTrip(), tripDeal.getDeliver()),
                        new ShipmentDealDTO(
                            dealRepository.findById(offersRepository.findByTripDealId(tripDeal.getId()).get(0).getShipmentDealId()).get(),
                            List.copyOf(
                                dealRepository
                                    .findById(offersRepository.findByTripDealId(tripDeal.getId()).get(0).getShipmentDealId())
                                    .get()
                                    .getShipments()
                            ),
                            dealRepository
                                .findById(offersRepository.findByTripDealId(tripDeal.getId()).get(0).getShipmentDealId())
                                .get()
                                .getOwner()
                        ),
                        offersRepository.findByTripDealId(tripDeal.getId()).get(0).getStatus(),
                        offersRepository.findByTripDealId(tripDeal.getId()).get(0).getSenderId()
                    )
                );
            }
        }

        List<Deal> shipmentDeals = dealRepository.findByOwner(user, pageable);
        for (Deal shipmentDeal : shipmentDeals) {
            if (offersRepository.findByShipmentDealId(shipmentDeal.getId()).size() > 0) {
                offersDTOs.add(
                    new OffersDTO(
                        offersRepository.findByShipmentDealId(shipmentDeal.getId()).get(0).getId(),
                        new TripDealDTO(
                            dealRepository
                                .findById(offersRepository.findByShipmentDealId(shipmentDeal.getId()).get(0).getTripDealId())
                                .get(),
                            dealRepository
                                .findById(offersRepository.findByShipmentDealId(shipmentDeal.getId()).get(0).getTripDealId())
                                .get()
                                .getTrip(),
                            dealRepository
                                .findById(offersRepository.findByShipmentDealId(shipmentDeal.getId()).get(0).getTripDealId())
                                .get()
                                .getDeliver()
                        ),
                        new ShipmentDealDTO(shipmentDeal, List.copyOf(shipmentDeal.getShipments()), shipmentDeal.getOwner()),
                        offersRepository.findByShipmentDealId(shipmentDeal.getId()).get(0).getStatus(),
                        offersRepository.findByShipmentDealId(shipmentDeal.getId()).get(0).getSenderId()
                    )
                );
            }
        }

        return offersDTOs;
    }
}
