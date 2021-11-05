package com.epam.esm.repository;

import com.epam.esm.exception.PaginationException;
import com.epam.esm.validation.PaginationValidator;
import org.springframework.stereotype.Component;

import static com.epam.esm.PaginationErrorType.INVALID_PAGE_NUMBER;
import static com.epam.esm.PaginationErrorType.INVALID_PAGE_SIZE;

@Component
public class PaginationContext {

    private PaginationValidator paginationValidator;

    private int page;
    private int pageSize;

    private PaginationContext(){
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
