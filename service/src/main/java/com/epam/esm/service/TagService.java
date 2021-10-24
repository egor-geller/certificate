package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link Tag} entity.
 *
 * @author Geller Egor
 */
@Service
public interface TagService extends EntityService<TagDto> {

    /**
     * Finds all stored tags
     *
     * @return list of {@link Tag} entity.
     */
    List<TagDto> findAllTags();

    /**
     * Fina a tag by its unique Id
     *
     * @param id id of a tag
     * @return {@link TagDto} entity.
     * @throws EntityNotFoundException when there is no such entity
     */
    TagDto findTagById(Long id);

    /**
     * Find a tag by its name
     *
     * @param tagName name of the tag
     * @return {@link TagDto} entity.
     * @throws EntityNotFoundException when there is no such entity
     * @throws InvalidEntityException  when the name of the tag is not correctly written
     */
    TagDto findTagByName(String tagName);

    /**
     * Create a new tag
     *
     * @param tag {@link Tag} entity.
     * @return {@link TagDto} entity.
     * @throws EntityAlreadyExistsException when {@link Tag} already exists
     * @throws InvalidEntityException       when {@link Tag} is not correctly written
     */
    TagDto create(TagDto tag);

    /**
     * Delete a tag
     *
     * @param id id of the tag
     * @return {@code true} if {@link Tag} was deleted, otherwise {@code false}
     * @throws InvalidEntityException when id is not correctly written
     */
    boolean delete(Long id);
}
