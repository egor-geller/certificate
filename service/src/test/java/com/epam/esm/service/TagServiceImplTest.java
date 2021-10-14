package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagServiceMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.repository.mapper.TagMapper;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.TagValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepositoryImpl tagRepository;

    @Mock
    private CertificateValidator certificateValidator;

    @Mock
    private TagValidator tagValidator;

    @Spy
    private TagServiceMapper tagMapper;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(TagServiceImplTest.class);
    }

    @Test
    void findAllTagsTest() {
        when(tagRepository.findAll()).thenReturn(tagList());

        List<TagDto> expectedTagDtoList = tagDtoList();
        List<TagDto> actualAllTagsService = tagService.findAllTagsService();

        assertEquals(expectedTagDtoList.toString(), actualAllTagsService.toString());
    }

    @Test
    void findByIdTest() {
        long id = 1;
        when(tagRepository.findById(id)).thenReturn(Optional.of(tag()));
        TagDto expectedTagDto = tagDto();
        TagDto actualTagByIdService = tagService.findTagByIdService(id);

        assertEquals(expectedTagDto.toString(), actualTagByIdService.toString());
    }

    @Test
    void findByNameTest() {
        String tagName = "testTagName1";
        when(tagRepository.findByName(tagName)).thenReturn(Optional.of(tag()));
        TagDto expectedTagDto = tagDto();
        TagDto actualTagByNameService = tagService.findTagByNameService(tagName);

        assertEquals(expectedTagDto.toString(), actualTagByNameService.toString());
    }

    @Test
    void createTest() {
        Tag tag = tag();
        TagDto tagDto = tagDto();
        when(tagRepository.findByName(tagDto.getName())).thenReturn(Optional.empty());

        tagService.createTagService(tagDto);

        verify(tagRepository).findByName(tag.getName());
        verify(tagRepository).create(tag);
    }
    private Tag tag() {
        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("testTagName1");

        return tag;
    }

    private TagDto tagDto() {
        TagDto tag = new TagDto();
        tag.setId(0L);
        tag.setName("testTagName1");

        return tag;
    }

    private List<Tag> tagList() {
        List<Tag> tagList = new ArrayList<>();

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("testTagName1");
        tagList.add(tag1);

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("testTagName2");
        tagList.add(tag2);

        Tag tag3 = new Tag();
        tag3.setId(3L);
        tag3.setName("testTagName3");
        tagList.add(tag3);

        return tagList;
    }

    private List<TagDto> tagDtoList() {
        List<TagDto> tagDtoList = new ArrayList<>();

        TagDto tagDto1 = new TagDto();
        tagDto1.setId(1L);
        tagDto1.setName("testTagName1");
        tagDtoList.add(tagDto1);

        TagDto tagDto2 = new TagDto();
        tagDto2.setId(2L);
        tagDto2.setName("testTagName2");
        tagDtoList.add(tagDto2);

        TagDto tagDto3 = new TagDto();
        tagDto3.setId(3L);
        tagDto3.setName("testTagName3");
        tagDtoList.add(tagDto3);

        return tagDtoList;
    }
}
