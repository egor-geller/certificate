package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Class containing public REST API endpoints related to {@link Tag} entity.
 *
 * @author Egor Geller
 */
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagServiceImpl tagService;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    @Autowired
    public TagController(TagServiceImpl tagService) {
        this.tagService = tagService;
    }

    /**
     * Gets all tags.
     *
     * @return JSON {@link ResponseEntity} object that contains list of {@link TagDto}
     */
    @GetMapping
    public ResponseEntity<Object> getAllTags() {
        List<TagDto> tagDtoList = tagService.findAllTags();
        return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
    }

    /**
     * Gets tag by its unique id
     *
     * @param id tag id
     * @return JSON {@link ResponseEntity} object that contains {@link TagDto} object
     * @throws EntityNotFoundException in case certificate with this id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTag(@PathVariable("id") Long id) {
        TagDto tagById = tagService.findTagById(id);
        return new ResponseEntity<>(tagById, HttpStatus.OK);
    }

    /**
     * Create new tag entity.
     *
     * @param tagDto {@link TagDto} instance
     * @return JSON {@link ResponseEntity} object that contains status {@code HttpStatus.CREATED} and {@link TagDto} object
     * @throws InvalidEntityException       when the content of {@link TagDto} instance is not correctly written
     * @throws EntityAlreadyExistsException when {@link Tag} entity is already exists
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createTag(@RequestBody TagDto tagDto) {
        TagDto tag = tagService.create(tagDto);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    /**
     * Delete existing tag.
     *
     * @param id tag id
     * @return {@code HttpStatus.NO_CONTENT} when entity has been deleted
     * @throws InvalidEntityException when id is not written correctly
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable("id") Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
