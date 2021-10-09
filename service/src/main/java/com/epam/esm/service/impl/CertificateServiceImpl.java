package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validator.CertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final CertificateMapper certificateMapper;
    private final CertificateValidator certificateValidator;

    public CertificateServiceImpl(CertificateRepository certificateRepository,
                                  TagRepository tagRepository,
                                  @Autowired CertificateMapper certificateMapper,
                                  @Autowired CertificateValidator certificateValidator) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.certificateMapper = certificateMapper;
        this.certificateValidator = certificateValidator;
    }

    @Override
    public List<CertificateDto> findCertificateByCriteriaService(SearchCriteria searchCriteria) {

        if (certificateValidator.areParamsEmpty(searchCriteria.getTagName(), searchCriteria.getCertificateName()
                , searchCriteria.getCertificateDescription(), searchCriteria.getSortByName().toString(),
                searchCriteria.getSortByCreateDate().toString())) {
            throw new InvalidEntityException("All parameters are empty");
        }

        List<Certificate> certificateList = certificateRepository.find(searchCriteria);

        return certificateMapper.changeListOfCertificatesToDto(certificateList, tagRepository);
    }

    @Override
    public CertificateDto findCertificateByIdService(long id) {
        if (id < 1) {
            throw new InvalidEntityException("The id isn't correctly written: " + id);
        }

        return certificateMapper.changeCertificateToDto(certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity by this id: " + id)), List.of());
    }

    @Override
    public boolean attachTagToCertificateService(long certificateId, long tagId) {
        if (certificateId < 1 || tagId < 1) {
            throw new InvalidEntityException("Both id's not been written correctly: " + certificateId + ", " + tagId);
        }
        return certificateRepository.attachTag(certificateId, tagId);
    }

    @Override
    public boolean detachTagFromCertificateService(long certificateId, long tagId) {
        if (certificateId < 1 || tagId < 1) {
            throw new InvalidEntityException("Both id's not been written correctly: " + certificateId + ", " + tagId);
        }
        return certificateRepository.detachTag(certificateId, tagId);
    }

    @Override
    public void createCertificateService(CertificateDto certificate) {
        if (certificate == null) {
            throw new InvalidEntityException("Certificate is null");
        }

        Certificate fromCertificateDto = certificateMapper.changeCertificateFromDto(certificate);
        Optional<Certificate> byId = certificateRepository.findById(fromCertificateDto.getId());
        if (byId.isPresent()) {
            throw new EntityAlreadyExistsException("Certificate is already exits");
        }

        certificateRepository.create(fromCertificateDto);
    }

    @Override
    public boolean updateCertificateService(CertificateDto certificate) {
        if (certificate == null || !certificateValidator.isCertificateDtoValid(certificate)) {
            throw new InvalidEntityException("Certificate is null");
        }

        Certificate fromCertificateDto = certificateMapper.changeCertificateFromDto(certificate);

        return certificateRepository.update(fromCertificateDto);
    }

    @Override
    public boolean deleteCertificateService(long id) {
        if (id < 1) {
            throw new InvalidEntityException("Id is not written correctly " + id);
        }
        return certificateRepository.delete(id);
    }
}
