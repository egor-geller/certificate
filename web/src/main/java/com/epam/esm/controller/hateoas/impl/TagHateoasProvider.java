package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.hateoas.ModelHateoasProvider;
import com.epam.esm.dto.TagDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.ALL_TAGS_REL;

@Component
public class TagHateoasProvider extends ModelHateoasProvider<TagDto> {

    @Override
    protected List<Link> addSpecificLinks(List<Link> baseLinks, TagDto model) {
        return baseLinks;
    }

    @Override
    protected Class<?> getControllerClass() {
        return TagController.class;
    }

    @Override
    protected String getAllResourcesRel() {
        return ALL_TAGS_REL;
    }
}
