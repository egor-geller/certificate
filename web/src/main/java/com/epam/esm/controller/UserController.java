package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.controller.hateoas.model.HateoasModel;
import com.epam.esm.controller.hateoas.model.ListHateoasModel;
import com.epam.esm.dto.UserDto;
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

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final PaginationContext paginationContext;
    private final HateoasProvider<UserDto> modelHateoasProvider;
    private final HateoasProvider<List<UserDto>> listHateoasProvider;

    @Autowired
    public UserController(UserService userService,
                          PaginationContext paginationContext,
                          HateoasProvider<UserDto> modelHateoasProvider,
                          HateoasProvider<List<UserDto>> listHateoasProvider) {
        this.userService = userService;
        this.paginationContext = paginationContext;
        this.modelHateoasProvider = modelHateoasProvider;
        this.listHateoasProvider = listHateoasProvider;
    }

    @GetMapping
    public ResponseEntity<ListHateoasModel<UserDto>> getAllUsers(@RequestParam(required = false) Integer page,
                                                                 @RequestParam(required = false) Integer pageSize) {
        List<UserDto> userDtoList = userService.findAllUsers(paginationContext.createPagination(page, pageSize));
        ListHateoasModel<UserDto> model = ListHateoasModel.build(listHateoasProvider, userDtoList);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HateoasModel<UserDto>> getUserById(@PathVariable("id") Long id) {
        UserDto userByIdService = userService.findUserByIdService(id);
        HateoasModel<UserDto> model = HateoasModel.build(modelHateoasProvider, userByIdService);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}