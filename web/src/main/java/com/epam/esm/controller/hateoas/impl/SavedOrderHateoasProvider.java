package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.hateoas.ModelHateoasProvider;
import com.epam.esm.dto.SavedOrderDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.ALL_ORDERS_REL;

@Component
public class SavedOrderHateoasProvider extends ModelHateoasProvider<SavedOrderDto> {
    @Override
    protected List<Link> addSpecificLinks(List<Link> baseLinks, SavedOrderDto model) {
        return baseLinks;
    }

    @Override
    protected Class<?> getControllerClass() {
        return OrderController.class;
    }

    @Override
    protected String getAllResourcesRel() {
        return ALL_ORDERS_REL;
    }
}
