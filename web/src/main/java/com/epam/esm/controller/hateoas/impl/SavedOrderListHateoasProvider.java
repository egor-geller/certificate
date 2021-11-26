package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.dto.SavedOrderDto;
import com.epam.esm.repository.PaginationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.ALL_ORDERS_REL;

@Component
public class SavedOrderListHateoasProvider extends ListHateoasProvider<SavedOrderDto> {

    @Autowired
    protected SavedOrderListHateoasProvider(PaginationContext paginationContext) {
        super(paginationContext);
    }

    @Override
    protected Class<?> getControllerClass() {
        return OrderController.class;
    }

    @Override
    protected String getAllResourcesRel() {
        return ALL_ORDERS_REL;
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
