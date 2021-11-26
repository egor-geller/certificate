package com.epam.esm.repository;

import com.epam.esm.validation.PaginationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaginationContext {

    private final PaginationValidator paginationValidator;

    private int page;
    private int pageSize;

    @Autowired
    public PaginationContext(PaginationValidator paginationValidator) {
        this.paginationValidator = paginationValidator;
    }

    public PaginationContext createPagination(Integer page, Integer pageSize) {
        this.page = paginationValidator.pageValidator(page);
        this.pageSize = paginationValidator.pageSizeValidator(pageSize);

        return this;
    }

    public int getStartPage() {
        return pageSize * (page - 1);
    }

    public int getLengthOfContext() {
        return pageSize;
    }
}
