package org.closure.laser.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.closure.laser.domain.UserApplication;
import org.closure.laser.service.UserApplicationService;
import org.closure.laser.service.dto.ShipmentDealDTO;
import org.closure.laser.service.dto.TripDealDTO;
import org.closure.laser.web.rest.vm.JWTToken;
import org.closure.laser.web.rest.vm.LoginVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link org.closure.laser.domain.UserApplication}.
 */
@RestController
@RequestMapping("/api")
public class UserApplicationResource {

    private final Logger log = LoggerFactory.getLogger(UserApplicationResource.class);

    private static final String ENTITY_NAME = "userApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserApplicationService userApplicationService;

    public UserApplicationResource(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    /**
     * {@code POST  /user-applications} : Create a new userApplication.
     *
     * @param userApplication the userApplication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new userApplication, or with status
     *         {@code 400 (Bad Request)} if the userApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-applications/register")
    public ResponseEntity<UserApplication> createUserApplication(@RequestBody UserApplication userApplication) throws URISyntaxException {
        log.debug("REST request to save UserApplication : {}", userApplication);

        UserApplication result = userApplicationService.save(userApplication);
        return ResponseEntity
            .created(new URI("/api/user-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/user-applications/OAuth")
    public ResponseEntity<UserApplication> createUserApplicationOAuth(@RequestBody UserApplication userApplication)
        throws URISyntaxException {
        log.debug("REST request to save UserApplication : {}", userApplication);
        UserApplication result = userApplicationService.saveOAuth(userApplication);
        return ResponseEntity
            .created(new URI("/api/user-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/user-applications/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        return userApplicationService.authenticate(loginVM);
    }

    @PostMapping("/user-applications/refreshToken")
    public ResponseEntity<JWTToken> refreshToken(@RequestHeader("Authorization") String token) {
        return userApplicationService.refreshToken(token.substring("Bearer ".length()));
    }

    @PostMapping("/user-applications/sendEmail")
    public ResponseEntity<UserApplication> sendEmail(@RequestHeader("Authorization") String token) {
        return userApplicationService.sendEmail(token.substring("Bearer ".length()));
    }

    /**
     * {@code PUT  /user-applications/:id} : Updates an existing userApplication.
     *
     * @param id              the id of the userApplication to save.
     * @param userApplication the userApplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated userApplication,
     *         or with status {@code 400 (Bad Request)} if the userApplication is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         userApplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-applications/{id}")
    public ResponseEntity<UserApplication> updateUserApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserApplication userApplication
    ) throws URISyntaxException {
        log.debug("REST request to update UserApplication : {}, {}", id, userApplication);

        UserApplication result = userApplicationService.update(id, userApplication);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userApplication.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-applications/:id} : Partial updates given fields of an
     * existing userApplication, field will ignore if it is null
     *
     * @param id              the id of the userApplication to save.
     * @param userApplication the userApplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated userApplication,
     *         or with status {@code 400 (Bad Request)} if the userApplication is
     *         not valid,
     *         or with status {@code 404 (Not Found)} if the userApplication is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         userApplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-applications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserApplication> partialUpdateUserApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserApplication userApplication
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserApplication partially : {}, {}", id, userApplication);

        Optional<UserApplication> result = userApplicationService.partialUpdate(id, userApplication);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userApplication.getId().toString())
        );
    }

    /**
     * {@code GET  /user-applications} : get all the userApplications.
     *
     * @param pageable  the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of userApplications in body.
     */
    @GetMapping("/user-applications")
    public ResponseEntity<List<UserApplication>> getAllUserApplications(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserApplications");
        return userApplicationService.getAllUserApplications(pageable, eagerload);
    }

    /**
     * {@code GET  /user-applications/:id} : get the "id" userApplication.
     *
     * @param id the id of the userApplication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the userApplication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-applications/{id}")
    public ResponseEntity<UserApplication> getUserApplication(@PathVariable Long id) {
        log.debug("REST request to get UserApplication : {}", id);
        Optional<UserApplication> userApplication = userApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userApplication);
    }

    /**
     * {@code GET  /user-applications/:id} : get the "id" userApplication.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the userApplication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-applications/login={login}")
    public ResponseEntity<UserApplication> getUserApplicationByLogin(@PathVariable String login) {
        log.debug("REST request to get UserApplication : {}", login);
        Optional<UserApplication> userApplication = userApplicationService.findOneByLogin(login);
        return ResponseUtil.wrapOrNotFound(userApplication);
    }

    /**
     * {@code DELETE  /user-applications/:id} : delete the "id" userApplication.
     *
     * @param id the id of the userApplication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-applications/{id}")
    public ResponseEntity<Void> deleteUserApplication(@PathVariable Long id) {
        log.debug("REST request to delete UserApplication : {}", id);
        userApplicationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /user-applications/getAllTripsDealsByUserAppId/:id} : get all the
     * tripsDeals by user id.
     *
     * @param id       id of user
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of Deal in body.
     */
    @GetMapping("/user-applications/getAllTripsDealsByUserAppId/{id}")
    public ResponseEntity<List<TripDealDTO>> getAllTripsDealsByUserAppId(
        @PathVariable Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of trip deals");
        List<TripDealDTO> page = userApplicationService.getAllTripsDealsByUserAppId(id, pageable);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /user-applications/getAllShipmentDealsByUserAppId/:id} : get all
     * the shipmentDeals by user id.
     *
     * @param id       id of user
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of Deal in body.
     */
    @GetMapping("/user-applications/getAllShipmentDealsByUserAppId/{id}")
    public ResponseEntity<List<ShipmentDealDTO>> getAllShipmentDealsByUserAppId(
        @PathVariable Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of shipment deals");
        List<ShipmentDealDTO> page = userApplicationService.getAllShipmentDealsByUserAppId(id, pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/user-applications/search/{value}")
    public ResponseEntity<List<UserApplication>> search(
        @PathVariable("value") String value,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search a page of transation");
        Page<UserApplication> page = userApplicationService.search(value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/user-applications/rate/{r}/{u_id}")
    public boolean rate(@PathVariable(name = "r") Long r, @PathVariable(name = "u_id") Long u_id) {
        return userApplicationService.rate(r, u_id);
    }

    @PostMapping("/user-applications/updateFCM/{fcmToken}")
    public void updateFCM(@RequestHeader("Authorization") String token, @PathVariable(name = "fcmToken") String fcmToken) {
        userApplicationService.updateFCM(token.substring("Bearer ".length()), fcmToken);
    }

    @PostMapping(value = "/upload-files")
    public ResponseEntity<List<Object>> uploadUserImages(
        @RequestParam("files") MultipartFile[] files,
        @RequestParam String type,
        @RequestParam Long id
    ) {
        return userApplicationService.multiUpload(files, type, id);
    }

    @GetMapping(value = "/assets/images/{type}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String type, @PathVariable String fileName) {
        return userApplicationService.downloadFileFromLocal(type, fileName);
    }
}
