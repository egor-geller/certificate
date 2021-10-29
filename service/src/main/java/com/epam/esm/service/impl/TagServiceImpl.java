package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.AttachedTagException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;
    private final TagServiceMapper tagServiceMapper;

    public TagServiceImpl(TagRepository tagRepository,
                          CertificateRepository certificateRepository,
                          TagValidator tagValidator,
                          @Autowired TagServiceMapper tagServiceMapper) {
        this.tagRepository = tagRepository;
        this.certificateRepository = certificateRepository;
        this.tagValidator = tagValidator;
        this.tagServiceMapper = tagServiceMapper;
    }

    @Override
    public List<TagDto> findAllTags() {
        return tagRepository.findAll().stream()
                .map(tagServiceMapper::convertTagToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findTagById(Long id) {
        tagValidator.validateId(id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return tagServiceMapper.convertTagToDto(tag);
    }

    @Override
    public TagDto findTagByName(String tagName) {
        tagValidator.validateTagValid(tagName);

        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException(tagName));

        return tagServiceMapper.convertTagToDto(tag);
    }

    @Override
    public TagDto create(TagDto tagDto) {
        tagValidator.validateTagValid(tagDto.getName());

        Tag fromTagDto = tagServiceMapper.convertTagFromDto(tagDto);

        Optional<Tag> maybeFoundTag = tagRepository.findByName(fromTagDto.getName());
        if (maybeFoundTag.isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        Tag newTag = tagRepository.create(fromTagDto);
        Optional<Tag> tag = tagRepository.findById(newTag.getId());
        if (tag.isEmpty()) {
            throw new EntityNotFoundException(newTag.getId());
        }
        return tagServiceMapper.convertTagToDto(tag.get());
    }

    @Override
    public TagDto update(TagDto tagDto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) {
        tagValidator.validateId(id);

        SearchCriteria searchCriteria = new SearchCriteria();
        Optional<Tag> tagById = tagRepository.findById(id);
        if (tagById.isEmpty()) {
            throw new EntityNotFoundException(id);
        }
        tagById.ifPresent(tag -> searchCriteria.setTagList(List.of(tag)));
        List<Certificate> certificateList = certificateRepository.find(searchCriteria);

        if (!certificateList.isEmpty()) {
            throw new AttachedTagException(tagById.get().getId(), tagById.get().getName());
        }

        tagRepository.delete(tagById.get());
        return true;
    }
}
