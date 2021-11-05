package com.epam.esm.repository;

import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Component;

import static com.epam.esm.PaginationErrorType.INVALID_PAGE_NUMBER;
import static com.epam.esm.PaginationErrorType.INVALID_PAGE_SIZE;

@Component
public class PaginationContext {

    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 0;
    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private int page;
    private int pageSize;

    private PaginationContext(){}

    public PaginationContext of(Integer page, Integer pageSize) {
        if (page == null) {
            page = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        this.page = page;
        this.pageSize = pageSize;

        if (page < MIN_PAGE) {
            throw new PaginationException(INVALID_PAGE_NUMBER, page);
        }

        if (pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
            throw new PaginationException(INVALID_PAGE_SIZE, pageSize);
        }

        return this;
    }

    public int getStartPage() {
        return pageSize * (page - 1);
    }

    public int getLengthOfContext() {
        return pageSize;
    }
}
