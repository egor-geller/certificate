package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityConnectedException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
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
                          @Autowired TagValidator tagValidator,
                          @Autowired TagServiceMapper tagServiceMapper) {
        this.tagRepository = tagRepository;
        this.certificateRepository = certificateRepository;
        this.tagValidator = tagValidator;
        this.tagServiceMapper = tagServiceMapper;
    }

    @Override
    public List<TagDto> findAllTagsService() {
        return tagRepository.findAll().stream()
                .map(tagServiceMapper::toTagDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findTagByIdService(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no tag by this id: " + id));
        return tagServiceMapper.toTagDto(tag);
    }

    @Override
    public TagDto findTagByNameService(String tagName) {
        boolean tagValid = tagValidator.isTagValid(tagName);
        if (tagValid) {
            throw new InvalidEntityException("Tag name is not valid " + tagName);
        }

        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("There is no tag by this name: " + tagName));

        return tagServiceMapper.toTagDto(tag);
    }

    @Override
    public void createTagService(TagDto tag) {
        if (tagValidator.isTagValid(tag.getName())) {
            throw new InvalidEntityException("Tag is not invalid " + tag.getName());
        }

        Tag fromTagDto = tagServiceMapper.fromTagDto(tag);

        Optional<Tag> isTagExists = tagRepository.findByName(fromTagDto.getName());
        if (isTagExists.isPresent()) {
            throw new EntityAlreadyExistsException("Tag already exists!");
        }

        tagRepository.create(fromTagDto);
    }

    @Override
    public boolean deleteTagService(Long id) {
        if (id == null || id < 1) {
            throw new InvalidEntityException("Id cannot be null");
        }

        SearchCriteria searchCriteria = new SearchCriteria();
        Optional<Tag> tagById = tagRepository.findById(id);
        searchCriteria.setTagName(tagById.toString());
        List<Certificate> certificateList = certificateRepository.find(searchCriteria);

        if (!certificateList.isEmpty()){
            throw new EntityConnectedException("Tag is connected to a certificate");
        }

        return tagRepository.delete(id);
    }
}
