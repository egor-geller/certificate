package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides functionality for manipulating {@link Tag} com.epam.esm.entity.
 *
 * @author Geller Egor
 */
public interface TagRepository extends Repository<Tag> {

    /**
     * Finds all stored tags
     *
     * @return list of {@link Tag} com.epam.esm.entity.
     */
    List<Tag> findAll();

    /**
     * Find a tag by its name
     *
     * @param tagName name of the tag
     * @return {@link Optional} of {@link Tag} com.epam.esm.entity.
     */
    Optional<Tag> findByName(String tagName);

    /**
     * Find tag by a certificate id
     *
     * @param id id of the certificate
     * @return list of {@link Tag} com.epam.esm.entity.
     */
    List<Tag> findByCertificateId(Long id);
}
