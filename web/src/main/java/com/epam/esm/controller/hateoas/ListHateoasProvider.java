package com.epam.esm.controller.hateoas;

import com.epam.esm.dto.IdDto;
import com.epam.esm.repository.PaginationContext;
import org.springframework.hateoas.Link;

import java.util.List;

import static com.epam.esm.controller.hateoas.impl.ResourceRelName.CURRENT_PAGE_REL;
import static com.epam.esm.controller.hateoas.impl.ResourceRelName.FIRST_PAGE_REL;
import static com.epam.esm.controller.hateoas.impl.ResourceRelName.LAST_PAGE_REL;
import static com.epam.esm.controller.hateoas.impl.ResourceRelName.NEXT_PAGE_REL;
import static com.epam.esm.controller.hateoas.impl.ResourceRelName.PREV_PAGE_REL;

public abstract class ListHateoasProvider<T extends IdDto> implements HateoasProvider<List<T>> {

    private final PaginationContext paginationContext;

    protected ListHateoasProvider(PaginationContext paginationContext) {
        this.paginationContext = paginationContext;
    }

    @Override
    public List<Link> provide(List<T> object, Long numberOfRecords) {
        Class<?> controllerClass = getControllerClass();
        String allResourcesRel = getAllResourcesRel();

        int currentPage = getCurrentPage(paginationContext);
        int pageSize = getPageSize(paginationContext);

        int nextPage = (currentPage / pageSize) + 2;
        int lastPageNumber = (int) ((numberOfRecords / pageSize) + 1);
        int prevPage = (currentPage / pageSize);

        Link allResourcesLink = LinkConstructor.constructControllerLink(controllerClass, allResourcesRel);
        Link firstPage = createPageLink(1, pageSize, FIRST_PAGE_REL);
        Link lastPage = createPageLink(lastPageNumber, pageSize, LAST_PAGE_REL);

        Link prev;
        if (currentPage > 2) {
            prev = createPageLink(prevPage, pageSize, PREV_PAGE_REL);
        } else {
            prev = createPageLink(currentPage + 1, pageSize, CURRENT_PAGE_REL);
        }

        Link next;
        if (nextPage < lastPageNumber + 1) {
            next = createPageLink(nextPage, pageSize, NEXT_PAGE_REL);
        }else {
            return List.of(allResourcesLink, firstPage, lastPage);
        }

        return List.of(allResourcesLink, next, prev, firstPage, lastPage);
    }

    protected abstract Class<?> getControllerClass();

    protected abstract String getAllResourcesRel();

    protected abstract int getCurrentPage(PaginationContext paginationContext);

    protected abstract int getPageSize(PaginationContext paginationContext);

    private Link createPageLink(int pageNumber, int pageSize, String rel) {
        Class<?> controllerClass = getControllerClass();
        return LinkConstructor.constructPageLink(controllerClass, pageNumber, pageSize, rel);
    }
}
