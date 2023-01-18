package org.closure.laser.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.validation.Valid;
import org.closure.laser.domain.Connection;
import org.closure.laser.domain.Deal;
import org.closure.laser.domain.Shipment;
import org.closure.laser.domain.Trip;
import org.closure.laser.domain.User;
import org.closure.laser.domain.UserApplication;
import org.closure.laser.repository.DealRepository;
import org.closure.laser.repository.ShipmentRepository;
import org.closure.laser.repository.TripRepository;
import org.closure.laser.repository.UserApplicationRepository;
import org.closure.laser.repository.UserRepository;
import org.closure.laser.security.jwt.JWTFilter;
import org.closure.laser.security.jwt.TokenProvider;
import org.closure.laser.service.dto.AdminUserDTO;
import org.closure.laser.service.dto.ShipmentDealDTO;
import org.closure.laser.service.dto.TripDealDTO;
import org.closure.laser.web.rest.errors.BadRequestAlertException;
import org.closure.laser.web.rest.vm.JWTToken;
import org.closure.laser.web.rest.vm.LoginVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * Service Implementation for managing {@link UserApplication}.
 */
@Service
@Transactional
public class UserApplicationService {

    private final Logger log = LoggerFactory.getLogger(UserApplicationService.class);

    private static final String ENTITY_NAME = "userApplication";

    private final UserApplicationRepository userApplicationRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private DealService dealService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserApplicationService(UserApplicationRepository userApplicationRepository) {
        this.userApplicationRepository = userApplicationRepository;
    }

    /**
     * Save a userApplication.
     *
     * @param userApplication the entity to save.
     * @return the persisted entity.
     */
    public UserApplication save(UserApplication userApplication) {
        log.debug("Request to save UserApplication : {}", userApplication);
        if (userApplication.getId() != null) {
            throw new BadRequestAlertException("A new userApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }

        AdminUserDTO userDto = new AdminUserDTO();

        userDto.setLogin(userApplication.getUser().getLogin());
        userDto.setPassword(userApplication.getUser().getPassword());
        userDto.setFirstName(userApplication.getUser().getFirstName());
        userDto.setLastName(userApplication.getUser().getLastName());
        userDto.setEmail(userApplication.getUser().getEmail());
        userDto.setLangKey(userApplication.getUser().getLangKey());
        userDto.setImageUrl(userApplication.getUser().getImageUrl());

        User savedUser = userService.registerUser(userDto, userDto.getPassword());
        userService.activateRegistration(savedUser.getActivationKey());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            savedUser.getLogin(),
            userDto.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, 2629746000L); // mounth
        String jwtRefrsh = tokenProvider.createToken(authentication, 31556952000L); // year

        Connection connection = new Connection();
        connection.setFcmToken(userApplication.getConnection().getFcmToken());
        connection.setoAuthToken(userApplication.getConnection().getoAuthToken());
        connection.setLocalToken(jwt);
        connection.setLocalRefreshToken(jwtRefrsh);
        connection.setLocalTokenExpiryDate(tokenProvider.getExpirationDate(jwt)); // mounth

        Connection savedConnection = connectionService.save(connection);

        userApplication.setConnection(savedConnection);
        savedUser.setActivated(!savedUser.isActivated());
        userApplication.setUser(savedUser);
        userApplication.setRate(0.0);
        userApplication.setOneStar(0.0);
        userApplication.setTwoStar(0.0);
        userApplication.setThreeStar(0.0);
        userApplication.setFourSatr(0.0);
        userApplication.setFiveStar(0.0);

        UserApplication savedUserApplication = userApplicationRepository.save(userApplication);

        return userApplicationRepository.findOneWithEagerRelationships(savedUserApplication.getId()).get();
    }

    public UserApplication saveOAuth(UserApplication userApplication) {
        log.debug("Request to save UserApplication : {}", userApplication);
        if (userApplication.getId() != null) {
            throw new BadRequestAlertException("A new userApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        boolean activationStatus = userApplication.getUser().isActivated();
        AdminUserDTO userDto = new AdminUserDTO();

        userDto.setLogin(userApplication.getUser().getLogin());
        userDto.setPassword(userApplication.getUser().getPassword());
        userDto.setFirstName(userApplication.getUser().getFirstName());
        userDto.setLastName(userApplication.getUser().getLastName());
        userDto.setEmail(userApplication.getUser().getEmail());
        userDto.setActivated(false);
        userDto.setLangKey(userApplication.getUser().getLangKey()); //
        userDto.setImageUrl(userApplication.getUser().getImageUrl());

        User savedUser = userService.registerUser(userDto, userDto.getPassword());
        userService.activateRegistration(savedUser.getActivationKey());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            savedUser.getLogin(),
            userDto.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, 2629746000L); // mounth
        String jwtRefrsh = tokenProvider.createToken(authentication, 31556952000L); // year//

        Connection connection = new Connection();
        connection.setFcmToken(userApplication.getConnection().getFcmToken());
        connection.setoAuthToken(userApplication.getConnection().getoAuthToken());
        connection.setLocalToken(jwt);
        connection.setLocalRefreshToken(jwtRefrsh);
        connection.setLocalTokenExpiryDate(tokenProvider.getExpirationDate(jwt)); // 30 days

        Connection savedConnection = connectionService.save(connection);

        userApplication.setConnection(savedConnection);
        userApplication.setUser(savedUser);
        userApplication.getUser().setActivated(activationStatus);
        userApplication.getUser().setActivationKey(new Random().nextInt(9999) + "");
        userApplication.setRate(0.0);
        userApplication.setOneStar(0.0);
        userApplication.setTwoStar(0.0);
        userApplication.setThreeStar(0.0);
        userApplication.setFourSatr(0.0);
        userApplication.setFiveStar(0.0);
        UserApplication savedUserApplication = userApplicationRepository.save(userApplication);

        return userApplicationRepository.findOneWithEagerRelationships(savedUserApplication.getId()).get();
    }

    public ResponseEntity<JWTToken> authenticate(@Valid LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, 2629746000L); // mounth
        String jwtRefrsh = tokenProvider.createToken(authentication, 31556952000L); // year

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        UserApplication userApplication = userApplicationRepository
            .findByUser(userRepository.findOneByLogin(loginVM.getUsername()).get())
            .get();

        userApplication.getConnection().setLocalToken(jwt);
        userApplication.getConnection().localRefreshToken(jwtRefrsh);
        userApplication.getConnection().setLocalTokenExpiryDate(tokenProvider.getExpirationDate(jwt)); // mounth

        userApplicationRepository.save(userApplication);

        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity<JWTToken> refreshToken(String token) {
        UserApplication userApplication = userApplicationRepository
            .findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get())
            .get();

        String jwt = tokenProvider.createRefreshToken(
            userApplication.getUser().getLogin(),
            2629746000L,
            tokenProvider.getAuthorities(token)
        ); // mounth

        String jwtRefrsh = tokenProvider.createRefreshToken(
            userApplication.getUser().getLogin(),
            31556952000L,
            tokenProvider.getAuthorities(token)
        ); // year

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        userApplication.getConnection().setFcmToken(userApplication.getConnection().getFcmToken());
        userApplication.getConnection().setoAuthToken(userApplication.getConnection().getoAuthToken());
        userApplication.getConnection().setLocalToken(jwt);
        userApplication.getConnection().setLocalRefreshToken(jwtRefrsh);
        userApplication.getConnection().setLocalTokenExpiryDate(tokenProvider.getExpirationDate(jwt)); // mounth

        userApplicationRepository.save(userApplication);

        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity<UserApplication> sendEmail(String token) {
        UserApplication userApplication = userApplicationRepository
            .findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get())
            .get();
        mailService.sendActivationEmail(userApplication.getUser());
        return ResponseEntity.ok().body(userApplication);
    }

    // @PostConstruct
    // public void testEmail(){
    //     User user = userRepository.findAll().get(1);
    //     user.setActivated(false);
    //     user.setLangKey("en");
    //     user.setEmail("anas.anas1998.tar@gmail.com");
    //     user.setActivationKey("32423");
    //     mailService.sendActivationEmail(user);
    //     log.info("sent");
    // }

    /**
     * Update a userApplication.
     *
     * @param userApplication the entity to save.
     * @return the persisted entity.
     */
    public UserApplication update(Long id, UserApplication userApplication) {
        log.debug("Request to save UserApplication : {}", userApplication);
        if (userApplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userApplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return userApplicationRepository.save(userApplication);
    }

    /**
     * Partially update a userApplication.
     *
     * @param userApplication the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserApplication> partialUpdate(Long id, UserApplication userApplication) {
        log.debug("Request to partially update UserApplication : {}", userApplication);
        if (userApplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userApplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "notfound");
        }
        return userApplicationRepository
            .findById(userApplication.getId())
            .map(existingUserApplication -> {
                if (userApplication.getPhone() != null) {
                    existingUserApplication.setPhone(userApplication.getPhone());
                }
                if (userApplication.getPassport() != null) {
                    existingUserApplication.setPassport(userApplication.getPassport());
                }
                if (userApplication.getCreatedAt() != null) {
                    existingUserApplication.setCreatedAt(userApplication.getCreatedAt());
                }
                if (userApplication.getIsGoogleAccount() != null) {
                    existingUserApplication.setIsGoogleAccount(userApplication.getIsGoogleAccount());
                }
                if (userApplication.getIsFacebookAccount() != null) {
                    existingUserApplication.setIsFacebookAccount(userApplication.getIsFacebookAccount());
                }
                if (userApplication.getIsTwitterAccount() != null) {
                    existingUserApplication.setIsTwitterAccount(userApplication.getIsTwitterAccount());
                }
                if (userApplication.getImage() != null) {
                    existingUserApplication.setImage(userApplication.getImage());
                }
                if (userApplication.getUser() != null) {
                    existingUserApplication.setUser(userApplication.getUser());
                }

                return existingUserApplication;
            })
            .map(userApplicationRepository::save);
    }

    public ResponseEntity<List<UserApplication>> getAllUserApplications(Pageable pageable, boolean eagerload) {
        log.debug("REST request to get a page of UserApplications");
        Page<UserApplication> page;
        if (eagerload) {
            page = findAllWithEagerRelationships(pageable);
        } else {
            page = findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Get all the userApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserApplication> findAll(Pageable pageable) {
        log.debug("Request to get all UserApplications");
        return userApplicationRepository.findAll(pageable);
    }

    /**
     * Get all the userApplications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserApplication> findAllWithEagerRelationships(Pageable pageable) {
        return userApplicationRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one userApplication by id.
     *
     * @param login the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserApplication> findOneByLogin(String login) {
        log.debug("Request to get UserApplication : {}", login);
        return userApplicationRepository.findByUser(userRepository.findOneWithAuthoritiesByLogin(login).get());
    }

    public Optional<UserApplication> findOne(Long id) {
        log.debug("Request to get UserApplication : {}", id);
        return userApplicationRepository.findById(id);
    }

    /**
     * Delete the userApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserApplication : {}", id);
        if (!userApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        userApplicationRepository.deleteById(id);
    }

    public List<TripDealDTO> getAllTripsDealsByUserAppId(Long id, Pageable pageable) {
        if (!userApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "notfound");
        }

        List<Deal> deals = dealRepository.findByDeliver(userApplicationRepository.findById(id).get(), pageable);

        List<TripDealDTO> list = new ArrayList<>();

        for (Deal deal : deals) {
            list.add(new TripDealDTO(deal, tripRepository.findById(deal.getTrip().getId()).get(), deal.getDeliver()));
        }

        return list;
    }

    public List<ShipmentDealDTO> getAllShipmentDealsByUserAppId(Long id, Pageable pageable) {
        if (!userApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "notfound");
        }

        List<Deal> deals = dealRepository.findByOwner(userApplicationRepository.findById(id).get(), pageable);

        List<ShipmentDealDTO> list = new ArrayList<>();

        for (Deal deal : deals) {
            list.add(new ShipmentDealDTO(deal, dealService.getAllShipmentsByDealId(deal.getId()), deal.getOwner()));
        }

        return list;
    }

    @Transactional(readOnly = true)
    public Page<UserApplication> search(String value, Pageable pageable) {
        log.debug("Request to get all user application by value");
        return userApplicationRepository.search(value, pageable);
    }

    public boolean rate(Long r, Long u_id) {
        UserApplication user = userApplicationRepository.findById(u_id).get();
        double oldRate = user.getRate();
        if (r == 1) {
            user.setOneStar(user.getOneStar() + 1);
        } else if (r == 2) {
            user.setTwoStar(user.getTwoStar() + 1);
        } else if (r == 3) {
            user.setThreeStar(user.getThreeStar() + 1);
        } else if (r == 4) {
            user.setFourSatr(user.getFourSatr() + 1);
        } else if (r == 5) {
            user.setFiveStar(user.getFiveStar() + 1);
        } else {
            throw new BadRequestAlertException("Rate out of bound", ENTITY_NAME, "rateerror");
        }

        user.setRate(
            (
                (5 * user.getFiveStar()) +
                (4 * user.getFourSatr()) +
                (3 * user.getThreeStar()) +
                (2 * user.getTwoStar()) +
                (1 * user.getOneStar())
            ) /
            (user.getFiveStar() + user.getFourSatr() + user.getThreeStar() + user.getTwoStar() + user.getOneStar())
        );

        UserApplication savedUser = userApplicationRepository.save(user);
        if (savedUser.getRate() != oldRate) return true; else return false;
    }

    @Transactional
    public void updateFCM(String token, String fcmToken) {
        UserApplication userApplication = userApplicationRepository
            .findByUser(userRepository.findOneByLogin(tokenProvider.getLoginFromToken(token)).get())
            .get();

        userApplication.getConnection().setFcmToken(fcmToken);

        userApplicationRepository.save(userApplication);
    }

    private static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }

    public ResponseEntity<List<Object>> multiUpload(MultipartFile[] files, String type, Long id) {
        List<Object> fileDownloadUrls = new ArrayList<>();
        Arrays
            .asList(files)
            .stream()
            .forEach(withCounter((i, file) -> fileDownloadUrls.add(uploadToLocalFileSystem(file, type, id, i).getBody())));
        return ResponseEntity.ok(fileDownloadUrls);
    }

    private ResponseEntity<Object> uploadToLocalFileSystem(MultipartFile file, String type, Long id, int counter) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(String.format("assets/images/%s/%d_%d%s", type, id, counter, fileName));
        path.toFile().getParentFile().mkdirs();
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileDownloadUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/api/assets/images/")
            .path(String.format("%s/%d_%d%s", type, id, counter, fileName))
            .toUriString();

        if (type.equals("user")) {
            UserApplication user = userApplicationRepository.findById(id).get();
            user.setImage(fileDownloadUri);
            userApplicationRepository.save(user);
        }
        if (type.equals("trip")) {
            Trip trip = tripRepository.findById(id).get();
            trip.setTicketImage(fileDownloadUri);
            tripRepository.save(trip);
        }
        if (type.equals("shipment")) {
            Shipment shipment = shipmentRepository.findById(id).get();
            if (shipment.getImgUrl().equals("")) shipment.setImgUrl(fileDownloadUri); else shipment.setImgUrl(
                shipment.getImgUrl() + "," + fileDownloadUri
            );
            shipmentRepository.save(shipment);
        }
        return ResponseEntity.ok(fileDownloadUri);
    }

    public ResponseEntity<Resource> downloadFileFromLocal(String type, String fileName) {
        Path path = Paths.get(String.format("assets/images/%s/%s", type, fileName));
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType("multipart/form-data"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
}
