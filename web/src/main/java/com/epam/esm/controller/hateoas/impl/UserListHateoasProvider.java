package com.epam.esm.controller.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.dto.UserDto;
import com.epam.esm.repository.PaginationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.ALL_USERS_REL;

@Component
public class UserListHateoasProvider extends ListHateoasProvider<UserDto> {

    @Autowired
    public UserListHateoasProvider(PaginationContext paginationContext) {
        super(paginationContext);
    }

    @Override
    protected Class<?> getControllerClass() {
        return UserController.class;
    }

    @Override
    protected String getAllResourcesRel() {
        return ALL_USERS_REL;
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
