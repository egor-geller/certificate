package com.epam.esm.repository.impl;

import com.epam.esm.config.DatabaseConfiguration;
import com.epam.esm.entity.Certificate;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfiguration.class)
@Transactional
class TagRepositoryTest {

    @Autowired
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
        tag.setName("tag2");
        Optional<Tag> expectedTag = tagRepository.findByName(tag.getName());
        assertEquals(expectedTag, Optional.empty());

        tagRepository.create(tag);
        Optional<Tag> expectedTagAfterCreation = tagRepository.findByName(tag.getName());
        assertEquals(expectedTagAfterCreation, Optional.of(tag));
    }

    @Test
    void deleteTest() {
        boolean delete = tagRepository.delete(2L);
        assertTrue(delete);
    }

    private List<Tag> tagList() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setName("tag1");
        tags.add(tag);
        return tags;
    }

    private Tag tag() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");
        return tag;
    }
}