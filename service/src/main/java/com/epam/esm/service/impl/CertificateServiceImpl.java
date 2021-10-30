package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.mapper.CertificateServiceMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validator.CertificateValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final CertificateServiceMapper certificateServiceMapper;
    private final CertificateValidator certificateValidator;

    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagRepository tagRepository,
                                  @Autowired CertificateServiceMapper certificateServiceMapper,
                                  CertificateValidator certificateValidator) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.certificateServiceMapper = certificateServiceMapper;
        this.certificateValidator = certificateValidator;
    }

    @Override
    public List<CertificateDto> findCertificateByCriteria(SearchCriteria searchCriteria) {
        certificateValidator.validateSearchCriteriaEmpty(searchCriteria);
        List<Certificate> certificateList = certificateRepository.find(searchCriteria).stream()
                .distinct()
                .collect(Collectors.toList());

        return certificateList.stream().map(certificate -> {
            long id = certificate.getId();
            List<Tag> byCertificateId = tagRepository.findByCertificateId(id);
            return certificateServiceMapper.convertCertificateToDto(certificate, byCertificateId);
        }).collect(Collectors.toList());
    }

    @Override
    public CertificateDto findCertificateById(long id) {
        certificateValidator.validateId(id);
        Certificate certificate = certificateRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(id));
        List<Tag> byCertificateId = tagRepository.findByCertificateId(id);
        return certificateServiceMapper.convertCertificateToDto(certificate, byCertificateId);
    }

    @Override
    public boolean attachTagToCertificate(long certificateId, long tagId) {
        certificateValidator.validateId(certificateId, tagId);
        Optional<Tag> tag = tagRepository.findById(tagId);
        Optional<Certificate> certificate = certificateRepository.findById(certificateId);
        if (tag.isEmpty() || certificate.isEmpty()) {
            throw new EntityNotFoundException(tagId, certificateId);
        }
        certificateRepository.attachTag(certificateId, tagId);
        return true;
    }

    @Override
    public boolean detachTagFromCertificate(long certificateId, long tagId) {
        certificateValidator.validateId(certificateId, tagId);

        SearchCriteria searchCriteria = new SearchCriteria();
        Optional<Tag> tag = tagRepository.findById(tagId);
        Optional<Certificate> certificate = certificateRepository.findById(certificateId);
        if (tag.isEmpty() || certificate.isEmpty()) {
            throw new EntityNotFoundException(tagId, certificateId);
        }

        searchCriteria.setTagList(List.of(tag.get().getName()));
        List<Certificate> certificateList = certificateRepository.find(searchCriteria);
        if (CollectionUtils.isEmpty(certificateList)) {
            tagRepository.delete(tag.get());
        }

        certificateRepository.detachTag(certificateId, tagId);
        return true;
    }

    @Override
    public CertificateDto create(CertificateDto certificate) {
        if (certificate == null) {
            throw new EntityNotFoundException();
        }

        boolean certificateDtoValid = certificateValidator.isCertificateDtoValid(certificate);

        if (!certificateDtoValid) {
            throw new InvalidEntityException(Certificate.class);
        }

        SearchCriteria searchCriteria = new SearchCriteria();
        Certificate fromCertificateDto = certificateServiceMapper.convertCertificateFromDto(certificate);
        searchCriteria.setCertificateName(fromCertificateDto.getName());
        List<Certificate> certificateList = certificateRepository.find(searchCriteria);
        if (!certificateList.isEmpty()) {
            throw new EntityAlreadyExistsException();
        }

        Certificate certificate1 = certificateRepository.create(fromCertificateDto);
        List<Tag> tagList = certificate.getTagList();
        if (tagList != null) {
            tagList.forEach(tagName -> {
                Optional<Tag> tagByName = tagRepository.findByName(tagName.getName());
                if (tagByName.isEmpty()) {
                    Tag createTag = new Tag();
                    createTag.setName(tagName.getName());
                    Tag tag = tagRepository.create(createTag);
                    attachTagToCertificate(certificate1.getId(), tag.getId());
                } else {
                    attachTagToCertificate(certificate1.getId(), tagByName.get().getId());
                }
            });
        }
        Optional<Certificate> createdCertificate = certificateRepository.findById(certificate1.getId());
        List<Tag> updatedTagList = tagRepository.findByCertificateId(certificate1.getId());
        return certificateServiceMapper.convertCertificateToDto(certificate1.getId(), createdCertificate, updatedTagList);
    }

    @Override
    public CertificateDto update(CertificateDto certificate) {
        boolean allParametersAreTheSame;
        if (certificate == null || certificate.getId() == null) {
            throw new EntityNotFoundException();
        }

        Optional<Certificate> certificateById = certificateRepository.findById(certificate.getId());
        if (certificateById.isEmpty()) {
            throw new EntityNotFoundException(certificate.getId());
        }
        certificateValidator.isParamsRegexValid(certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration());
        Certificate checkedCertificate = certificateValidator.checkForUpdatedFields(certificate, certificateById);

        allParametersAreTheSame = certificateValidator.areAllParametersTheSame(certificateById, checkedCertificate);

        List<Tag> tagList = certificate.getTagList();
        if (CollectionUtils.isNotEmpty(tagList)) {
            List<Tag> byCertificateId = tagRepository.findByCertificateId(checkedCertificate.getId());
            if (CollectionUtils.isNotEmpty(byCertificateId)) {
                boolean allMatch = tagList.stream()
                        .allMatch(tag -> (byCertificateId.stream()
                                .allMatch(tag1 -> tag.getName().equals(tag1.getName()))));
                if (allMatch && allParametersAreTheSame) {
                    throw new EntityAlreadyExistsException();
                }
                byCertificateId.forEach(tag -> detachTagFromCertificate(checkedCertificate.getId(), tag.getId()));
            }
            certificate.getTagList().forEach(tagName -> {
                Optional<Tag> tagIdByName = tagRepository.findByName(tagName.getName());
                if (tagIdByName.isEmpty()) {
                    Tag createTag = new Tag();
                    createTag.setName(tagName.getName());
                    Tag tag = tagRepository.create(createTag);
                    attachTagToCertificate(checkedCertificate.getId(), tag.getId());
                } else {
                    attachTagToCertificate(checkedCertificate.getId(), tagIdByName.get().getId());
                }
            });
        }

        Optional<Certificate> updatedCertificate = certificateRepository.findById(certificate.getId());
        List<Tag> updatedTagList = tagRepository.findByCertificateId(certificate.getId());
        return certificateServiceMapper.convertCertificateToDto(certificate.getId(), updatedCertificate, updatedTagList);
    }

    @Override
    public boolean delete(Long id) {
        certificateValidator.validateId(id);
        Optional<Certificate> certificateById = certificateRepository.findById(id);
        if (certificateById.isEmpty()) {
            throw new EntityNotFoundException(id);
        }
        List<Tag> byCertificateId = tagRepository.findByCertificateId(id);
        byCertificateId.forEach(tag -> detachTagFromCertificate(id, tag.getId()));

        certificateRepository.delete(certificateById.get());
        return true;
    }

    @Scheduled(cron = "0 30 9-17 * * MON-FRI")
    public void scheduleDeletionOfDetachedTag() {
        List<Tag> allTagsList = tagRepository.findAll();
        SearchCriteria searchCriteria = new SearchCriteria();
        for (Tag tag : allTagsList) {
            searchCriteria.setTagList(List.of(tag.getName()));
            List<Certificate> certificateList = certificateRepository.find(searchCriteria);
            if (certificateList == null || certificateList.isEmpty()) {
                tagRepository.delete(tag);
            }
        }
    }
}
