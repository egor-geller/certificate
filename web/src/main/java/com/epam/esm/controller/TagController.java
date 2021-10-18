package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Class containing public REST API endpoints related to {@link Tag} entity.
 *
 * @author Egor Geller
 */
@RestController
@RequestMapping("/api/{ver}/tags")
public class TagController {

    private final TagServiceImpl tagService;
    private static final String VERSION = "v1.0";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String ERROR_CODE = "errorCode";
    private static final String VERSION_ERROR = "versionError";

    private final ResourceBundleMessageSource bundleMessageSource;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    @Autowired
    public TagController(TagServiceImpl tagService, ResourceBundleMessageSource messageSource) {
        this.bundleMessageSource = messageSource;
        this.tagService = tagService;
    }

    /**
     * Gets all tags.
     *
     * @return JSON {@link ResponseEntity} object that contains list of {@link TagDto}
     */
    @GetMapping
    public ResponseEntity<Object> getAllTags(@PathVariable String ver) {
        if (Objects.equals(ver, VERSION)){
            List<TagDto> tagDtoList = tagService.findAllTags();
            return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
        }
        String errorMessage = getErrorMessage(VERSION_ERROR);
        return buildResponse(HttpStatus.MOVED_PERMANENTLY, errorMessage, HttpStatus.MOVED_PERMANENTLY.toString());

    }

    /**
     * Gets tag by its unique id
     *
     * @param id tag id
     * @return JSON {@link ResponseEntity} object that contains {@link TagDto} object
     * @throws EntityNotFoundException in case certificate with this id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTag(@PathVariable("id") Long id, @PathVariable String ver) {
        if (Objects.equals(ver, VERSION)) {
            TagDto tagById = tagService.findTagById(id);
            return new ResponseEntity<>(tagById, HttpStatus.OK);
        }
        String errorMessage = getErrorMessage(VERSION_ERROR);
        return buildResponse(HttpStatus.MOVED_PERMANENTLY, errorMessage, HttpStatus.MOVED_PERMANENTLY.toString());

    }

    /**
     * Create new tag entity.
     *
     * @param tagDto {@link TagDto} instance
     * @return JSON {@link ResponseEntity} object that contains status {@code HttpStatus.CREATED}
     * @throws InvalidEntityException       when the content of {@link TagDto} instance is not correctly written
     * @throws EntityAlreadyExistsException when {@link Tag} entity is already exists
     */
    @PostMapping
    public ResponseEntity<Object> createTag(@RequestBody TagDto tagDto, @PathVariable String ver) {
        if (Objects.equals(ver, VERSION)) {
            tagService.create(tagDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        String errorMessage = getErrorMessage(VERSION_ERROR);
        return buildResponse(HttpStatus.MOVED_PERMANENTLY, errorMessage, HttpStatus.MOVED_PERMANENTLY.toString());
    }

    /**
     * Delete existing tag.
     *
     * @param id tag id
     * @return {@code HttpStatus.OK} and {@code True} when entity has been deleted, otherwise,
     * {@code HttpStatus.NOT_MODIFIED} and {@code False}
     * @throws InvalidEntityException when id is not written correctly
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable("id") Long id, @PathVariable String ver) {
        if (Objects.equals(ver, VERSION)) {
            boolean hasBeenDeleted = tagService.delete(id);
            return hasBeenDeleted ? new ResponseEntity<>(hasBeenDeleted, HttpStatus.OK)
                    : new ResponseEntity<>(hasBeenDeleted, HttpStatus.NOT_MODIFIED);
        }
        String errorMessage = getErrorMessage(VERSION_ERROR);
        return buildResponse(HttpStatus.MOVED_PERMANENTLY, errorMessage, HttpStatus.MOVED_PERMANENTLY.toString());
    }

    private String getErrorMessage(String errorMessageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return bundleMessageSource.getMessage(errorMessageName, null, locale);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String errorMessage, String errorCode) {
        Map<String, Object> map = new HashMap<>();
        map.put(ERROR_MESSAGE, errorMessage);
        map.put(ERROR_CODE, errorCode);
        return new ResponseEntity<>(map, status);
    }
}
