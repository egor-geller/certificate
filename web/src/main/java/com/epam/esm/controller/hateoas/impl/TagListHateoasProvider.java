package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.dto.TagDto;
import com.epam.esm.repository.PaginationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.ALL_TAGS_REL;

@Component
public class TagListHateoasProvider extends ListHateoasProvider<TagDto> {

    @Autowired
    protected TagListHateoasProvider(PaginationContext paginationContext) {
        super(paginationContext);
    }

    @Override
    protected Class<?> getControllerClass() {
        return TagController.class;
    }

    @Override
    protected String getAllResourcesRel() {
        return ALL_TAGS_REL;
    }

    @Override
    protected int getCurrentPage(PaginationContext paginationContext) {
        return paginationContext.getStartPage();
    }

    @Override
    protected int getPageSize(PaginationContext paginationContext) {
        return paginationContext.getLengthOfContext();
    }
}
