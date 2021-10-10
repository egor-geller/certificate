package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides functionality for manipulating {@link Tag} entity.
 *
 * @author Geller Egor
 */

public interface TagService {

    /**
     * Finds all stored tags
     *
     * @return list of {@link Tag} entity.
     */
    List<TagDto> findAllTagsService();

    /**
     * Fina a tag by its unique Id
     *
     * @param id id of a tag
     * @return {@link Optional} of {@link Tag} entity.
     * @throws EntityNotFoundException when there is no such entity
     */
    TagDto findTagByIdService(Long id);

    /**
     * Find a tag by its name
     *
     * @param tagName name of the tag
     * @return {@link Optional} of {@link Tag} entity.
     * @throws EntityNotFoundException when there is no such entity
     * @throws InvalidEntityException  when the name of the tag is not correctly written
     */
    TagDto findTagByNameService(String tagName);

    /**
     * Create a new tag
     *
     * @param tag {@link Tag} entity.
     * @throws EntityAlreadyExistsException when {@link Tag} already exists
     * @throws InvalidEntityException       when {@link Tag} is not correctly written
     */
    void createTagService(TagDto tag);

    /**
     * Delete a tag
     *
     * @param id id of the tag
     * @return {@code true} if {@link Tag} was deleted, otherwise {@code false}
     * @throws InvalidEntityException when id is not correctly written
     */
    boolean deleteTagService(Long id);
}
