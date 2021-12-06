package com.epam.esm.controller.hateoas;

import com.epam.esm.dto.IdDto;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelHateoasProvider <T extends IdDto> implements HateoasProvider<T> {

    @Override
    public List<Link> provide(T object, Long numberOfRecords) {
        Class<?> controllerClass = getControllerClass();
        String allResourcesRel = getAllResourcesRel();
        Link selfLink = LinkConstructor.constructSelfLink(controllerClass, object);
        Link allResourcesLink = LinkConstructor.constructControllerLink(controllerClass, allResourcesRel);

        List<Link> links = new ArrayList<>();
        links.add(selfLink);
        links.add(allResourcesLink);

        return addSpecificLinks(links, object);
    }

    protected abstract List<Link> addSpecificLinks(List<Link> baseLinks, T model);
    protected abstract Class<?> getControllerClass();
    protected abstract String getAllResourcesRel();
}
