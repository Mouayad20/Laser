package org.closure.laser.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.closure.laser.domain.Location;
import org.closure.laser.repository.LocationRepository;
import org.closure.laser.service.dto.LocationModel;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    private static final String ENTITY_NAME = "location";

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        if (location.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "id exists");
        }
        if (locationRepository.findByCity(location.getCity()).isPresent()) {
            throw new BadRequestAlertException("A new location have an exists city name", ENTITY_NAME, "city exists");
        }

        return locationRepository.save(location);
    }

    public Location update(Long id, Location location) {
        log.debug("Request to save Location : {}", location);
        if (location.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, location.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }
        if (!locationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        if (locationRepository.findByCity(location.getCity()).isPresent()) {
            throw new BadRequestAlertException("A new location have an exists city name", ENTITY_NAME, "city exists");
        }
        return locationRepository.save(location);
    }

    public Optional<Location> partialUpdate(Long id, Location location) {
        log.debug("Request to partially update Location : {}", location);
        if (location.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, location.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }
        if (!locationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        if (locationRepository.findByCity(location.getCity()).isPresent()) {
            throw new BadRequestAlertException("A new location have an exists city name", ENTITY_NAME, "city exists");
        }

        return locationRepository.findById(locationRepository.save(location).getId());
        // return locationRepository
        // .findById(location.getId())
        // .map(existingLocation -> {
        // locationMapper.partialUpdate(existingLocation, location);

        // return existingLocation;
        // })
        // .map(locationRepository::save)
        // .map(locationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<Location> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Location> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        if (!locationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        locationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Location> search(String key, String value, Pageable pageable) {
        log.debug("Request to get all location by key");
        return locationRepository.search(key, value, pageable);
    }

    public List<Location> getFromAPI() {
        String token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiYjI2NGQ5YTdhYjE4YWY3YzhhNjFmNDg0ZWEzNTdhYTJkZWJiMGE5M2ExMTE1NGQ3MTdmZDAwOGQ3MGI5NTU3NTE3NzM0ZmIwNWQ1MTBkMGUiLCJpYXQiOjE2NjA1NTkzNzMsIm5iZiI6MTY2MDU1OTM3MywiZXhwIjoxNjkyMDk1MzczLCJzdWIiOiIxMDY0NCIsInNjb3BlcyI6W119.Dxp61zWOM2nto4St8chK6GSCZ2V9Xn6518KB12QPUzd_iR1Fa4YptZcnAghzukhqUkhDburIZsj2jKi0petnaw";
        String uri = "https://app.goflightlabs.com/airports?access_key=" + token;/* */
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LocationModel[]> responseEntity = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<LocationModel[]>() {}
        );

        LocationModel[] locationModels = responseEntity.getBody();
        Arrays
            .asList(locationModels)
            .stream()
            .map(model -> {
                Location location = new Location();
                if (!locationRepository.findByAirport(model.getAirport_name()).isPresent()) {
                    location.setCity(model.getTimezone().split("/")[1]);
                    location.setCountry(model.getCountry_name());
                    location.setAirport(model.getAirport_name());
                    location.setCreatedAt(new Date());
                    location = locationRepository.save(location);
                }
                return location;
            })
            .collect(Collectors.toList());
        return locationRepository.findAll();
    }
}
