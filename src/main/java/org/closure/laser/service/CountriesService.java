package org.closure.laser.service;

import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Countries;
import org.closure.laser.repository.CountriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Countries}.
 */
@Service
@Transactional
public class CountriesService {

    private final Logger log = LoggerFactory.getLogger(CountriesService.class);

    private final CountriesRepository countriesRepository;

    public CountriesService(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    /**
     * Save a countries.
     *
     * @param countries the entity to save.
     * @return the persisted entity.
     */
    public Countries save(Countries countries) {
        log.debug("Request to save Countries : {}", countries);
        return countriesRepository.save(countries);
    }

    /**
     * Update a countries.
     *
     * @param countries the entity to save.
     * @return the persisted entity.
     */
    public Countries update(Countries countries) {
        log.debug("Request to save Countries : {}", countries);
        return countriesRepository.save(countries);
    }

    /**
     * Partially update a countries.
     *
     * @param countries the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Countries> partialUpdate(Countries countries) {
        log.debug("Request to partially update Countries : {}", countries);

        return countriesRepository
            .findById(countries.getId())
            .map(existingCountries -> {
                if (countries.getCountry() != null) {
                    existingCountries.setCountry(countries.getCountry());
                }
                if (countries.getCapital() != null) {
                    existingCountries.setCapital(countries.getCapital());
                }
                if (countries.getCode() != null) {
                    existingCountries.setCode(countries.getCode());
                }
                if (countries.getPhoneCode() != null) {
                    existingCountries.setPhoneCode(countries.getPhoneCode());
                }

                return existingCountries;
            })
            .map(countriesRepository::save);
    }

    /**
     * Get all the countries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Countries> findAll() {
        log.debug("Request to get all Countries");
        return countriesRepository.findAll();
    }

    /**
     * Get one countries by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Countries> findOne(Long id) {
        log.debug("Request to get Countries : {}", id);
        return countriesRepository.findById(id);
    }

    /**
     * Delete the countries by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Countries : {}", id);
        countriesRepository.deleteById(id);
    }
}
