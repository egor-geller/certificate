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
    public List<CertificateDto> findCertificateByCriteriaService(SearchCriteria searchCriteria) {
        certificateValidator.isSearchCriteriaEmpty(searchCriteria);
        List<Certificate> certificateList = certificateRepository.find(searchCriteria);

        return certificateList.stream().map(certificate -> {
            long id = certificate.getId();
            List<Tag> byCertificateId = tagRepository.findByCertificateId(id);
            return certificateServiceMapper.convertCertificateToDto(certificate, byCertificateId);
        }).collect(Collectors.toList());
    }

    @Override
    public CertificateDto findCertificateByIdService(long id) {
        if (id < 1) {
            throw new EntityNotFoundException(id);
        }

        Certificate certificate = certificateRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(id));
        List<Tag> byCertificateId = tagRepository.findByCertificateId(id);

        return certificateServiceMapper.convertCertificateToDto(certificate, byCertificateId);
    }

    @Override
    public boolean attachTagToCertificateService(long certificateId, long tagId) {
        if (certificateId < 1 || tagId < 1) {
            throw new EntityNotFoundException(certificateId, tagId);
        }
        return certificateRepository.attachTag(certificateId, tagId);
    }

    @Override
    public boolean detachTagFromCertificateService(long certificateId, long tagId) {
        if (certificateId < 1 || tagId < 1) {
            throw new EntityNotFoundException(certificateId ,tagId);
        }

        SearchCriteria searchCriteria = new SearchCriteria();
        Optional<Tag> tag = tagRepository.findById(tagId);
        searchCriteria.setTagName(tag.toString());
        List<Certificate> certificateList = certificateRepository.find(searchCriteria);
        if (certificateList == null || certificateList.isEmpty()) {
            tagRepository.delete(tagId);
        }

        return certificateRepository.detachTag(certificateId, tagId);
    }

    @Override
    public void createCertificateService(CertificateDto certificate) {
        if (certificate == null) {
            throw new EntityNotFoundException();
        }
        boolean certificateDtoValid = certificateValidator.isCertificateDtoValid(certificate);

        if (!certificateDtoValid) {
            throw new InvalidEntityException(Certificate.class);
        }

        Certificate fromCertificateDto = certificateServiceMapper.convertCertificateFromDto(certificate);
        Optional<Certificate> byId = certificateRepository.findById(fromCertificateDto.getId());
        if (byId.isPresent() && byId.get().getName().equals(fromCertificateDto.getName())) {
            throw new EntityAlreadyExistsException();
        }

        certificateRepository.create(fromCertificateDto);
    }

    @Override
    public boolean updateCertificateService(CertificateDto certificate) {
        if (certificate == null) {
            throw new EntityNotFoundException();
        }

        boolean certificateDtoValid = certificateValidator.isCertificateDtoValid(certificate);

        if (!certificateDtoValid) {
            throw new InvalidEntityException(Certificate.class);
        }

        Certificate fromCertificateDto = certificateServiceMapper.convertCertificateFromDto(certificate);

        return certificateRepository.update(fromCertificateDto);
    }

    @Override
    public boolean deleteCertificateService(long id) {
        if (id < 1) {
            throw new EntityNotFoundException(id);
        }

        return certificateRepository.delete(id);
    }

    @Scheduled(fixedDelay = 3000)
    public void scheduleDeletionOfDetachedTag() {
        List<Tag> allTagsList = tagRepository.findAll();
        SearchCriteria searchCriteria = new SearchCriteria();
        for (Tag tag : allTagsList) {
            searchCriteria.setTagName(tag.getName());
            List<Certificate> certificateList = certificateRepository.find(searchCriteria);
            if (certificateList == null || certificateList.isEmpty()) {
                tagRepository.delete(tag.getId());
            }
        }
    }
}
