package com.epam.esm.controller.hateoas;

import com.epam.esm.dto.IdDto;
import com.epam.esm.repository.PaginationContext;
import org.springframework.hateoas.Link;

import java.util.List;

public abstract class ListHateoasProvider<T extends IdDto> implements HateoasProvider<List<T>> {

    private final PaginationContext paginationContext;

    protected ListHateoasProvider(PaginationContext paginationContext) {
        this.paginationContext = paginationContext;
    }

    @Override
    public List<Link> provide(List<T> object) {
        Class<?> controllerClass = getControllerClass();
        String allResourcesRel = getAllResourcesRel();

        int currentPage = getCurrentPage(paginationContext);
        int pageSize = getPageSize(paginationContext);

        Link allResourcesLink = LinkConstructor.constructControllerLink(controllerClass, allResourcesRel);
        Link next = LinkConstructor.constructPageLink(controllerClass, (currentPage / pageSize) + 2, pageSize, "Next page");
        Link prev;
        if (currentPage > 2) {
            prev = LinkConstructor.constructPageLink(controllerClass, (currentPage / pageSize), pageSize, "Prev page");
        } else {
            prev = LinkConstructor.constructPageLink(controllerClass, currentPage + 1, pageSize, "This page");
        }
        Link firstPage = LinkConstructor.constructPageLink(controllerClass, 1, pageSize, "First page");

        //TODO: how to find last page?
        Link lastPage = LinkConstructor.constructPageLink(controllerClass, pageSize, pageSize, "Last Page");

        return List.of(allResourcesLink, next, prev, firstPage, lastPage);
    }

    protected abstract Class<?> getControllerClass();

    protected abstract String getAllResourcesRel();

    protected abstract int getCurrentPage(PaginationContext paginationContext);

    protected abstract int getPageSize(PaginationContext paginationContext);

    private int sizeOfList() {
        return 18*50;
    }
}
