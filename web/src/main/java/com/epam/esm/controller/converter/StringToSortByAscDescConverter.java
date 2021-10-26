package com.epam.esm.controller.converter;

import com.epam.esm.repository.builder.SortType;
import org.springframework.core.convert.converter.Converter;

public class StringToSortByAscDescConverter implements Converter<String, SortType> {

    @Override
    public SortType convert(String source) {
        return SortType.valueOf(source.toUpperCase());
    }
}
