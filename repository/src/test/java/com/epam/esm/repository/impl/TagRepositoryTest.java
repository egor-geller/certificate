package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.PaginationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class)
@EntityScan(basePackages = "com.epam.esm")
class TagRepositoryTest {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private TagRepositoryImpl tagRepository;
    @Autowired
    private PaginationContext paginationContext;

    @Test
    void findAllTest() {
        List<Tag> tagList = tagRepository.findAll(paginationContext.createPagination(1, 10));
        assertEquals(0, tagList.size());
    }

    @Test
    void findByNameTest() {
        Tag actualTag = tag();
        Optional<Tag> expectedTag = tagRepository.findByName("Forward Infrastructure Designer");
        logger.info(expectedTag);
        assertEquals(Optional.empty(), expectedTag);
    }

   /* @Test
    void findByCertificateIdTest() {
        List<Tag> expectedTags = tagRepository.findByCertificateId(353L);
        List<Tag> actualTags = tagList();
        assertEquals(expectedTags, actualTags.get(0));
    }*/

    @Test
    @Transactional
    void createTest() {
        Tag tag = new Tag();
        tag.setName("tag21");
        Optional<Tag> expectedTag = tagRepository.findByName(tag.getName());
        assertEquals(expectedTag, Optional.empty());

        Tag tag1 = tagRepository.create(tag);
        tag.setId(tag1.getId());
        Optional<Tag> expectedTagAfterCreation = tagRepository.findByName(tag.getName());
        assertEquals(expectedTagAfterCreation, Optional.of(tag));
    }

    private List<Tag> tagList() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setId(39L);
        tag.setName("Forward Infrastructure Designer");
        tags.add(tag);
        Tag tag1 = new Tag();
        tag1.setId(40L);
        tag1.setName("Dynamic Response Orchestrator");
        tags.add(tag1);
        return tags;
    }

    private Tag tag() {
        Tag tag = new Tag();
        tag.setId(39L);
        tag.setName("Forward Infrastructure Designer");
        return tag;
    }
}
