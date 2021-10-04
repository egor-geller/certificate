package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.ParametersValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDto> findAllTagsService() {
        return tagRepository.findAll().stream()
                .map(TagMapper::toTagDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findTagByIdService(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityException("There is no tag by this id: " + id));
        return TagMapper.toTagDto(tag);
    }

    @Override
    public TagDto findTagByNameService(String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityException("There is no tag by this name: " + tagName));
        return TagMapper.toTagDto(tag);
    }

    @Override
    public void createTagService(TagDto tag) {
        Tag fromTagDto = TagMapper.fromTagDto(tag);

        if (ParametersValidator.isParamsValid(fromTagDto.getName())) {
            throw new EntityException("Parameters are invalid");
        }

        Optional<Tag> isTagExists = tagRepository.findByName(fromTagDto.getName());
        if (isTagExists.isPresent()) {
            throw new EntityException("Tag already exists!");
        }

        tagRepository.createTag(fromTagDto);
    }

    @Override
    public boolean deleteTagService(Long id) {
        if (id == null) {
            throw new EntityException("Id cannot be null");
        }
        return tagRepository.deleteTag(id);
    }
}
