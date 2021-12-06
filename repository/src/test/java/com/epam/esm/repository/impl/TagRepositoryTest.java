package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TagRepositoryTest {

   /* @Autowired
    private TagRepository tagRepository;

    @Test
    void findAllTest() {
        List<Tag> actualList = tagList();
        List<Tag> tagList = tagRepository.findAll();

        assertEquals(tagList.get(0), actualList.get(0));
    }

    @Test
    void findByNameTest() {
        Tag actualTag = tag();
        Optional<Tag> expectedTag = tagRepository.findByName("tag1");
        assertEquals(Optional.of(actualTag), expectedTag);
    }

    @Test
    void findByCertificateIdTest() {
        List<Tag> expectedTags = tagRepository.findByCertificateId(1L);
        List<Tag> actualTags = tagList();
        assertEquals(expectedTags, actualTags);
    }

    @Test
    void createTest() {
        Tag tag = new Tag();
        tag.setName("tag21");
        Optional<Tag> expectedTag = tagRepository.findByName(tag.getName());
        assertEquals(expectedTag, Optional.empty());

        Long aLong = tagRepository.create(tag);
        tag.setId(aLong);
        Optional<Tag> expectedTagAfterCreation = tagRepository.findByName(tag.getName());
        assertEquals(expectedTagAfterCreation, Optional.of(tag));
    }

    @Test
    void deleteTest() {
        boolean delete = tagRepository.delete(17L);
        assertTrue(delete);
    }

    private List<Tag> tagList() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setId(6L);
        tag.setName("content1");
        tags.add(tag);
        Tag tag1 = new Tag();
        tag1.setId(12L);
        tag1.setName("tag1");
        tags.add(tag1);
        return tags;
    }

    private Tag tag() {
        Tag tag = new Tag();
        tag.setId(12L);
        tag.setName("tag1");
        return tag;
    }*/
}
