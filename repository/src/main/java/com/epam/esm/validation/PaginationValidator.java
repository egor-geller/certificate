package com.epam.esm.validation;

import com.epam.esm.exception.PaginationException;
import org.springframework.stereotype.Component;

import static com.epam.esm.PaginationErrorType.INVALID_PAGE_NUMBER;
import static com.epam.esm.PaginationErrorType.INVALID_PAGE_SIZE;

@Component
public class PaginationValidator {

    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 0;
    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private PaginationValidator(){}

    public Integer pageValidator(Integer page) {
        if (page == null) {
            page = DEFAULT_PAGE;
        }

        if (page < MIN_PAGE) {
            throw new PaginationException(INVALID_PAGE_NUMBER, page);
        }

        return page;
    }

    public Integer pageSizeValidator(Integer pageSize) {
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
            throw new PaginationException(INVALID_PAGE_SIZE, pageSize);
        }
        return pageSize;
    }
}
