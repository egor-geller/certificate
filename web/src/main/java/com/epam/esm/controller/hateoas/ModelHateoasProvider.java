package com.epam.esm.controller.hateoas;

import com.epam.esm.dto.IdDto;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelHateoasProvider <T extends IdDto> implements HateoasProvider<T> {

    @Override
    public List<Link> provide(T object) {
        Class<?> controllerClass = getControllerClass();
        String allResourcesRel = getAllResourcesRel();
        Long currentId = getCurrentId(object);
        Link selfLink = LinkConstructor.constructSelfLink(controllerClass, object);
        Link allResourcesLink = LinkConstructor.constructControllerLink(controllerClass, allResourcesRel);
        Link next = LinkConstructor.constructControllerLinkWithId(controllerClass, currentId + 1, "next");

        List<Link> links = new ArrayList<>();
        links.add(selfLink);
        links.add(allResourcesLink);
        links.add(next);

        return addSpecificLinks(links, object);
    }

    protected abstract List<Link> addSpecificLinks(List<Link> baseLinks, T model);
    protected abstract Class<?> getControllerClass();
    protected abstract String getAllResourcesRel();
    protected abstract Long getCurrentId(T t);
}
