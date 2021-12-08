package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.controller.hateoas.model.HateoasModel;
import com.epam.esm.controller.hateoas.model.ListHateoasModel;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private final PaginationContext paginationContext;
    private final HateoasProvider<TagDto> modelHateoasProvider;
    private final ListHateoasProvider<TagDto> listHateoasProvider;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    @Autowired
    public TagController(TagServiceImpl tagService,
                         PaginationContext paginationContext,
                         HateoasProvider<TagDto> modelHateoasProvider,
                         ListHateoasProvider<TagDto> listHateoasProvider) {
        this.tagService = tagService;
        this.paginationContext = paginationContext;
        this.modelHateoasProvider = modelHateoasProvider;
        this.listHateoasProvider = listHateoasProvider;
    }

    /**
     * Gets all tags.
     *
     * @return JSON {@link ResponseEntity} object that contains list of {@link TagDto}
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ListHateoasModel<TagDto>> getAllTags(@RequestParam(required = false) Integer page,
                                                               @RequestParam(required = false) Integer pageSize) {
        List<TagDto> tagDtoList = tagService.findAllTags(paginationContext.createPagination(page, pageSize));
        Long count = tagService.count();
        ListHateoasModel<TagDto> model = new ListHateoasModel<>(tagDtoList);
        ListHateoasModel<TagDto> build = model.build(listHateoasProvider, tagDtoList, count);

        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    /**
     * Gets tag by its unique id
     *
     * @param id tag id
     * @return JSON {@link ResponseEntity} object that contains {@link TagDto} object
     * @throws EntityNotFoundException in case certificate with this id doesn't exist
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HateoasModel<TagDto>> getTag(@PathVariable("id") Long id) {
        TagDto tagById = tagService.findTagById(id);
        return createPagination(tagById, HttpStatus.OK);
    }

    @GetMapping("/mostusedtag")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HateoasModel<TagDto>> getMostWidelyTag() {
        TagDto tagDto = tagService.findMostWidelyUsedTag();
        return createPagination(tagDto, HttpStatus.OK);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HateoasModel<TagDto>> createTag(@RequestBody TagDto tagDto) {
        TagDto tag = tagService.create(paginationContext, tagDto);
        return createPagination(tag, HttpStatus.CREATED);
    }

    /**
     * Delete existing tag.
     *
     * @param id tag id
     * @return {@code HttpStatus.NO_CONTENT} when entity has been deleted
     * @throws InvalidEntityException when id is not written correctly
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteTag(@PathVariable("id") Long id) {
        tagService.delete(id, paginationContext);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<HateoasModel<TagDto>> createPagination(TagDto tagDto, HttpStatus status) {
        HateoasModel<TagDto> model = new HateoasModel<>(tagDto);
        HateoasModel<TagDto> build = model.build(modelHateoasProvider, tagDto, 1L);
        return new ResponseEntity<>(build, status);
    }
}
