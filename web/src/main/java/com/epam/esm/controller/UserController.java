package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.controller.hateoas.model.HateoasModel;
import com.epam.esm.controller.hateoas.model.ListHateoasModel;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class containing public REST API endpoints related to {@link User} entity.
 *
 * @author Egor Geller
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

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
    public ResponseEntity<HateoasModel<UserDto>> getUserById(@PathVariable("id") Long id) {
        UserDto userByIdService = userService.findUserByIdService(id);
        HateoasModel<UserDto> model = new HateoasModel<>(userByIdService);
        HateoasModel<UserDto> build = model.build(modelHateoasProvider, userByIdService, 1L);

        return new ResponseEntity<>(build, HttpStatus.OK);
    }
}
