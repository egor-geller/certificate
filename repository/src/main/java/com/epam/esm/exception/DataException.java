package com.epam.esm.exception;

import java.util.Arrays;

public class DataException extends RuntimeException{

    public DataException(Throwable cause, long... id) {
        super(Arrays.toString(id), cause);
    }
}
