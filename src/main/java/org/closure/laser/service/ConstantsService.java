package org.closure.laser.service;

import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.Constants;
import org.closure.laser.repository.ConstantsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Constants}.
 */
@Service
@Transactional
public class ConstantsService {

    private final Logger log = LoggerFactory.getLogger(ConstantsService.class);

    private final ConstantsRepository constantsRepository;

    public ConstantsService(ConstantsRepository constantsRepository) {
        this.constantsRepository = constantsRepository;
    }

    /**
     * Save a constants.
     *
     * @param constants the entity to save.
     * @return the persisted entity.
     */
    public Constants save(Constants constants) {
        log.debug("Request to save Constants : {}", constants);
        return constantsRepository.save(constants);
    }

    /**
     * Update a constants.
     *
     * @param constants the entity to save.
     * @return the persisted entity.
     */
    public Constants update(Constants constants) {
        log.debug("Request to save Constants : {}", constants);
        return constantsRepository.save(constants);
    }

    /**
     * Partially update a constants.
     *
     * @param constants the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Constants> partialUpdate(Constants constants) {
        log.debug("Request to partially update Constants : {}", constants);

        return constantsRepository
            .findById(constants.getId())
            .map(existingConstants -> {
                if (constants.getWeightFactor() != null) {
                    existingConstants.setWeightFactor(constants.getWeightFactor());
                }
                if (constants.getMaxWeight() != null) {
                    existingConstants.setMaxWeight(constants.getMaxWeight());
                }

                return existingConstants;
            })
            .map(constantsRepository::save);
    }

    /**
     * Get all the constants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Constants> findAll() {
        log.debug("Request to get all Constants");
        return constantsRepository.findAll();
    }

    /**
     * Get one constants by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Constants> findOne(Long id) {
        log.debug("Request to get Constants : {}", id);
        return constantsRepository.findById(id);
    }

    /**
     * Delete the constants by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Constants : {}", id);
        constantsRepository.deleteById(id);
    }
}
