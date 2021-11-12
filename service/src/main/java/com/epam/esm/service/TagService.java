package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
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
    List<TagDto> findAllTags(PaginationContext paginationContext);

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
     * Find most widely used tag
     *
     * @return {@link TagDto} entity.
     */
    TagDto findMostWidelyUsedTag();
}
