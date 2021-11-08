package com.epam.esm.controller;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final PaginationContext paginationContext;

    @Autowired
    public UserController(UserService userService, PaginationContext paginationContext) {
        this.userService = userService;
        this.paginationContext = paginationContext;
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getAllUsers(@RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer pageSize) {
        List<UserDto> userDtoList = userService.findAllUsers(paginationContext.createPagination(page, pageSize));
        List<UserDto> response = new ArrayList<>();
        userDtoList.forEach(user -> {
            user.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
            response.add(user);
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        UserDto userByIdService = userService.findUserByIdService(id);
        if (userByIdService != null) {
            userByIdService.add(linkTo(
                    methodOn(UserController.class)
                            .getAllUsers(paginationContext.getStartPage(), paginationContext.getLengthOfContext()))
                    .withSelfRel()
            );
        }
        return new ResponseEntity<>(userByIdService, HttpStatus.OK);
    }
}
