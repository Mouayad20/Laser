package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.closure.laser.domain.AccountProvider;
import org.closure.laser.domain.Transaction;
import org.closure.laser.service.AccountProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.closure.domain.AccountProvider}.
 */
@RestController
@RequestMapping("/api")
public class AccountProviderResource {

    private final Logger log = LoggerFactory.getLogger(AccountProviderResource.class);

    private static final String ENTITY_NAME = "accountProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountProviderService accountProviderService;

    public AccountProviderResource(AccountProviderService accountProviderService) {
        this.accountProviderService = accountProviderService;
    }

    /**
     * {@code POST  /account-providers} : Create a new accountProvider.
     *
     * @param accountProvider the accountProvider to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new accountProvider, or with status
     *         {@code 400 (Bad Request)} if the accountProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-providers")
    public ResponseEntity<AccountProvider> createAccountProvider(@RequestBody AccountProvider accountProvider) throws URISyntaxException {
        log.debug("REST request to save AccountProvider : {}", accountProvider);

        AccountProvider result = accountProviderService.save(accountProvider);
        return ResponseEntity
            .created(new URI("/api/account-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-providers/:id} : Updates an existing accountProvider.
     *
     * @param id              the id of the accountProvider to save.
     * @param accountProvider the accountProvider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated accountProvider,
     *         or with status {@code 400 (Bad Request)} if the accountProvider is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         accountProvider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-providers/{id}")
    public ResponseEntity<AccountProvider> updateAccountProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountProvider accountProvider
    ) throws URISyntaxException {
        log.debug("REST request to update AccountProvider : {}, {}", id, accountProvider);

        AccountProvider result = accountProviderService.update(id, accountProvider);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountProvider.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /account-providers} : get all the accountProviders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of accountProviders in body.
     */
    @GetMapping("/account-providers")
    public List<AccountProvider> getAllAccountProviders() {
        log.debug("REST request to get all AccountProviders");
        return accountProviderService.findAll();
    }

    /**
     * {@code GET  /account-providers/:id} : get the "id" accountProvider.
     *
     * @param id the id of the accountProvider to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the accountProvider, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-providers/{id}")
    public ResponseEntity<AccountProvider> getAccountProvider(@PathVariable Long id) {
        log.debug("REST request to get AccountProvider : {}", id);
        Optional<AccountProvider> accountProvider = accountProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountProvider);
    }

    /**
     * {@code DELETE  /account-providers/:id} : delete the "id" accountProvider.
     *
     * @param id the id of the accountProvider to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-providers/{id}")
    public ResponseEntity<Void> deleteAccountProvider(@PathVariable Long id) {
        log.debug("REST request to delete AccountProvider : {}", id);
        accountProviderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /account-providers/getAllTransactionsByAccoutProvId=:id} : get
     * page of transactions for specific account provider .
     *
     * @param pageable the pagination information.
     * @param id       the id of account provider to be get transactions for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of transactions in body.
     */
    @GetMapping("/deal-statuses/getAllTransactionsByAccoutProvId={id}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByAccoutProvId(
        @PathVariable("id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of transactions by account provider  id");
        Page<Transaction> page = accountProviderService.getAllTransactionsByAccoutProvId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/account-providers/search/{key}/{value}")
    public ResponseEntity<List<AccountProvider>> search(
        @PathVariable("key") String key,
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of account provider");
        Page<AccountProvider> page = accountProviderService.search(key, value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
