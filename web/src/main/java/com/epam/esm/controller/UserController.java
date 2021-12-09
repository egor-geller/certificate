package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.controller.hateoas.model.HateoasModel;
import com.epam.esm.controller.hateoas.model.ListHateoasModel;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Class containing public REST API endpoints related to {@link User} entity.
 *
 * @author Egor Geller
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger();

    private final UserService userService;
    private final PaginationContext paginationContext;
    private final HateoasProvider<UserDto> modelHateoasProvider;
    private final ListHateoasProvider<UserDto> listHateoasProvider;

    /**
     * Instantiates a new User controller.
     *
     * @param userService          {@link UserService} order service
     * @param paginationContext    {@link PaginationContext} pagination context
     * @param modelHateoasProvider {@link HateoasProvider} model hateoas provider
     * @param listHateoasProvider  {@link ListHateoasProvider} list hateoas provider
     */
    @Autowired
    public UserController(UserService userService,
                          PaginationContext paginationContext,
                          HateoasProvider<UserDto> modelHateoasProvider,
                          ListHateoasProvider<UserDto> listHateoasProvider) {
        this.userService = userService;
        this.paginationContext = paginationContext;
        this.modelHateoasProvider = modelHateoasProvider;
        this.listHateoasProvider = listHateoasProvider;
    }

    /**
     * Gets all users.
     *
     * @param page     {@code Integer} current page, not required, by default 1
     * @param pageSize {@code Integer} number of objects to display in a page, not required, by default 10
     * @return JSON {@link ResponseEntity} object that contains list {@link ListHateoasModel} of {@link UserDto}
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ListHateoasModel<UserDto>> getAllUsers(@RequestParam(required = false) Integer page,
                                                                 @RequestParam(required = false) Integer pageSize) {
        List<UserDto> userDtoList = userService.findAllUsers(paginationContext.createPagination(page, pageSize));
        Long count = userService.count();
        ListHateoasModel<UserDto> model = new ListHateoasModel<>(userDtoList);
        ListHateoasModel<UserDto> build = model.build(listHateoasProvider, userDtoList, count);

        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    /**
     * Gets user by id.
     *
     * @param id id of the {@link User} entity
     * @return JSON {@link ResponseEntity} with {@link HateoasModel} object that contains {@link UserDto} object
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HateoasModel<UserDto>> getUserById(@PathVariable("id") Long id) {
        UserDto userByIdService = userService.findUserByIdService(id);
        HateoasModel<UserDto> model = new HateoasModel<>(userByIdService);
        HateoasModel<UserDto> build = model.build(modelHateoasProvider, userByIdService, 1L);

        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    /**
     * Create a new user.
     * Access is allowed to everyone.
     *
     * @param userDto {@link UserDto} instance
     * @throws InvalidEntityException in case when passed DTO object contains invalid data
     * @throws EntityAlreadyExistsException in case when user with specified username already exists
     * @return JSON {@link ResponseEntity} with {@link HateoasModel} object that contains {@link UserDto} object
     */
    @PostMapping("/signup")
    public ResponseEntity<HateoasModel<UserDto>> signup(@RequestBody UserDto userDto) {
        UserDto signup = userService.signup(userDto);
        HateoasModel<UserDto> model = new HateoasModel<>(signup);
        HateoasModel<UserDto> build = model.build(modelHateoasProvider, signup, 1L);

        return new ResponseEntity<>(build, CREATED);
    }

    /**
     * Authenticate with provided credentials.
     * Access is allowed to everyone.
     *
     * @param userDto {@link UserDto} instance
     * @throws BadCredentialsException in case when provided credentials are wrong
     * @return JSON {@link ResponseEntity} with {@link HateoasModel} object that contains {@link UserDto} object
     */
    @PostMapping("/login")
    public ResponseEntity<HateoasModel<UserDto>> login(@RequestBody UserDto userDto) {
        logger.info("UserController - UserDto: {}", userDto);
        UserDto login = userService.login(userDto);
        logger.info("UserController - UserDto: {}", login);
        HateoasModel<UserDto> model = new HateoasModel<>(login);
        HateoasModel<UserDto> build = model.build(modelHateoasProvider, login, 1L);

        return new ResponseEntity<>(build, CREATED);
    }
}
