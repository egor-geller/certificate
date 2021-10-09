package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tagDtoList = tagService.findAllTagsService();
        return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable("id") Long id) {
        TagDto tagByIdService = tagService.findTagByIdService(id);
        return new ResponseEntity<>(tagByIdService, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createTag(@RequestBody TagDto tagDto) {
        tagService.createTagService(tagDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTag(@PathVariable("id") Long id) {
        boolean hasBeenDeleted = tagService.deleteTagService(id);
        return hasBeenDeleted ? new ResponseEntity<>(hasBeenDeleted, HttpStatus.OK)
                : new ResponseEntity<>(hasBeenDeleted, HttpStatus.NOT_MODIFIED);
    }
}
