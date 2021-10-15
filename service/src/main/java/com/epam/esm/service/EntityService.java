package com.epam.esm.service;

public interface EntityService<T> {

    void create(T t);

    boolean update(T t);

    boolean delete(Long id);
}
