package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides functionality for manipulating {@link Tag} entity.
 *
 * @author Geller Egor
 */
public interface TagRepository {

    /**
     * Finds all stored tags
     *
     * @return list of {@link Tag} entity.
     */
    List<Tag> findAll();

    /**
     * Fina a tag by its unique Id
     *
     * @param id id of a tag
     * @return {@link Optional} of {@link Tag} entity.
     */
    Optional<Tag> findById(Long id);

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

    /**
     * Create a new tag
     *
     * @param tag {@link Tag} entity.
     */
    void createTag(Tag tag);

    /**
     * Delete a tag
     *
     * @param id id of the tag
     * @return {@code true} if {@link Tag} was deleted, otherwise {@code false}
     */
    boolean deleteTag(Long id);
}
