package org.closure.laser.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.closure.laser.domain.AccountProvider;
import org.closure.laser.domain.Transaction;
import org.closure.laser.repository.AccountProviderRepository;
import org.closure.laser.repository.TransactionRepository;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountProvider}.
 */
@Service
@Transactional
public class AccountProviderService {

    private final Logger log = LoggerFactory.getLogger(AccountProviderService.class);

    private static final String ENTITY_NAME = "accountProvider";
    private final AccountProviderRepository accountProviderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public AccountProviderService(AccountProviderRepository accountProviderRepository) {
        this.accountProviderRepository = accountProviderRepository;
    }

    public AccountProvider save(AccountProvider accountProvider) {
        log.debug("Request to save AccountProvider : {}", accountProvider);
        if (accountProvider.getId() != null) {
            throw new BadRequestAlertException("A new accountProvider cannot already have an ID", ENTITY_NAME, "id exists");
        }
        if (accountProviderRepository.findByName(accountProvider.getName()).isPresent()) {
            throw new BadRequestAlertException("A new accountProvider have an exists name", ENTITY_NAME, "name exists");
        }

        return accountProviderRepository.save(accountProvider);
    }

    public AccountProvider update(Long id, AccountProvider accountProvider) {
        log.debug("Request to save AccountProvider : {}", accountProvider);
        if (accountProvider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (!Objects.equals(id, accountProvider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "id invalid");
        }

        if (!accountProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        if (accountProviderRepository.findByName(accountProvider.getName()).isPresent()) {
            throw new BadRequestAlertException("A new accountProvider have an exists name", ENTITY_NAME, "name exists");
        }
        return accountProviderRepository.save(accountProvider);
    }

    @Transactional(readOnly = true)
    public List<AccountProvider> findAll() {
        log.debug("Request to get all AccountProviders");
        return accountProviderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AccountProvider> findOne(Long id) {
        log.debug("Request to get AccountProvider : {}", id);
        return accountProviderRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete AccountProvider : {}", id);
        if (!accountProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        accountProviderRepository.deleteById(id);
    }

    public Page<Transaction> getAllTransactionsByAccoutProvId(Long id, Pageable pageable) {
        if (!accountProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "id not found");
        }
        return transactionRepository.findByProvider(accountProviderRepository.findById(id).get(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<AccountProvider> search(String key, String value, Pageable pageable) {
        log.debug("Request to get all account provider by key");
        return accountProviderRepository.search(key, value, pageable);
    }
}
