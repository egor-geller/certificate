package com.epam.esm.controller.hateoas;

import com.epam.esm.dto.IdDto;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkConstructor {

    private LinkConstructor() {
    }

    public static <T extends IdDto> Link constructSelfLink(Class<?> controllerClass, T model) {
        return linkTo(controllerClass)
                .slash(model.getId())
                .withSelfRel();
    }

    public static Link constructControllerLink(Class<?> controllerClass, String rel) {
        return linkTo(controllerClass)
                .withRel(rel);
    }

    public static Link constructControllerLinkWithId(Class<?> controllerClass, long id, String rel) {
        return linkTo(controllerClass)
                .slash(id)
                .withRel(rel);
    }
    public static Link constructPageLink(Class<?> controllerClass, int page, int pageSize, String rel) {
        return linkTo(controllerClass)
                .slash("?page="+page+"&pageSize="+pageSize)
                .withRel(rel);
    }
}
