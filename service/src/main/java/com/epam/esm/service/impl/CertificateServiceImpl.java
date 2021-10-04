package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.mapper.CertificateMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.SortType;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository, TagRepository tagRepository) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<CertificateDto> findCertificateByService(String tagName, String certificateName
            , String certificateDescription, SortType sortByName, SortType sortByCreateDate) {

        if (tagName.isEmpty() && certificateName.isEmpty() && certificateDescription.isEmpty()
                && sortByName == null && sortByCreateDate == null) {
            throw new EntityException("There are no parameters to find a certificate");
        }

        return certificateRepository.find(tagName, certificateName
                        , certificateDescription, sortByName, sortByCreateDate).stream()
                .map(certificate -> {
                    long certificateId = certificate.getId();
                    List<Tag> tagList = tagRepository.findByCertificateId(certificateId);
                    return CertificateMapper.toCertificateDto(certificate, tagList);
                }).collect(Collectors.toList());

    }

    @Override
    public CertificateDto findCertificateByIdService(long id) {
        if (id < 1) {
            throw new EntityException("There is no entity by this id: " + id);
        }
        return CertificateMapper.toCertificateDto(certificateRepository.findById(id)
                .orElseThrow(() -> new EntityException("There is no entity by this id: " + id)), List.of());
    }

    @Override
    public boolean attachTagToCertificateService(long certificateId, long tagId) {
        if (certificateId < 1 || tagId < 1) {
            return false;
        }
        return certificateRepository.attachTag(certificateId, tagId);
    }

    @Override
    public boolean detachTagFromCertificateService(long certificateId, long tagId) {
        if (certificateId < 1 || tagId < 1) {
            return false;
        }
        return certificateRepository.detachTag(certificateId, tagId);
    }

    @Override
    public void createCertificateService(CertificateDto certificate) {
        if (certificate == null) {
            throw new EntityException("Certificate is null");
        }

        Certificate fromCertificateDto = CertificateMapper.fromCertificateDto(certificate);
        Optional<Certificate> byId = certificateRepository.findById(fromCertificateDto.getId());
        if (byId.isPresent()) {
            throw new EntityException("Certificate is already exits");
        }

        certificateRepository.create(fromCertificateDto);
    }

    @Override
    public boolean updateCertificateService(CertificateDto certificate) {
        if (certificate == null) {
            return false;
        }

        Certificate fromCertificateDto = CertificateMapper.fromCertificateDto(certificate);

        return certificateRepository.update(fromCertificateDto);
    }

    @Override
    public boolean deleteCertificateService(long id) {
        if (id < 1) {
            return false;
        }
        return certificateRepository.delete(id);
    }
}
