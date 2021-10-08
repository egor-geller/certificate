package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides functionality for manipulating {@link Tag} entity.
 *
 * @author Geller Egor
 */
public interface TagRepository extends BaseRepository<Tag> {

    /**
     * Finds all stored tags
     *
     * @return list of {@link Tag} entity.
     */
    List<Tag> findAll();

    /**
     * Find a tag by its name
     *
     * @param tagName name of the tag
     * @return {@link Optional} of {@link Tag} entity.
     */
    Optional<Tag> findByName(String tagName);

    /**
     * Find tag by a certificate id
     *
     * @param id id of the certificate
     * @return list of {@link Tag} entity.
     */
    List<Tag> findByCertificateId(Long id);
}
