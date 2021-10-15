package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
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
                .toList();
    }

    @Override
    public TagDto findTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return tagServiceMapper.convertTagToDto(tag);
    }

    @Override
    public TagDto findTagByName(String tagName) {
        tagValidator.isTagValid(tagName);

        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException(tagName));

        return tagServiceMapper.convertTagToDto(tag);
    }

    @Override
    public void createTag(TagDto tag) {
        tagValidator.isTagValid(tag.getName());

        Tag fromTagDto = tagServiceMapper.convertTagFromDto(tag);

        Optional<Tag> isTagExists = tagRepository.findByName(fromTagDto.getName());
        if (isTagExists.isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        tagRepository.create(fromTagDto);
    }

    @Override
    public boolean deleteTag(Long id) {
        if (id == null || id < 1) {
            throw new EntityNotFoundException(id);
        }

        SearchCriteria searchCriteria = new SearchCriteria();
        Optional<Tag> tagById = tagRepository.findById(id);
        searchCriteria.setTagName(tagById.toString());
        List<Certificate> certificateList = certificateRepository.find(searchCriteria);

        if (!certificateList.isEmpty()){
            throw new EntityAlreadyExistsException();
        }

        return tagRepository.delete(id);
    }
}
