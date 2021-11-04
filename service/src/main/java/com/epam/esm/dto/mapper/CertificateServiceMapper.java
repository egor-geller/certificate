package com.epam.esm.dto.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CertificateServiceMapper {

    private CertificateServiceMapper() {
    }

    public CertificateDto convertCertificateToDto(Certificate certificate, List<Tag> tagName) {
        return new CertificateDto(certificate, tagName);
    }

    public CertificateDto convertCertificateToDto(Long updatedId, Optional<Certificate> updatedCertificate, List<Tag> byCertificateId) {
        if (updatedCertificate.isEmpty()) {
            throw new EntityNotFoundException(updatedId);
        }
        return convertCertificateToDto(updatedCertificate.get(), byCertificateId);
    }

    public Certificate convertCertificateFromDto(CertificateDto certificateDto) {
        Certificate certificate = new Certificate();
        if (certificateDto.getId() != null) {
            certificate.setId(certificateDto.getId());
        }
        if (certificateDto.getName() != null) {
            certificate.setName(certificateDto.getName());
        }
        if (certificateDto.getDescription() != null) {
            certificate.setDescription(certificateDto.getDescription());
        }
        if (certificateDto.getPrice() != null) {
            certificate.setPrice(certificateDto.getPrice());
        }
        if (certificateDto.getDuration() != null) {
            certificate.setDuration(certificateDto.getDuration());
        }
        if (certificateDto.getCreateDate() != null) {
            certificate.setCreateDate(certificateDto.getCreateDate());
        }
        if (certificateDto.getLastUpdateDate() != null) {
            certificate.setLastUpdateDate(certificateDto.getLastUpdateDate());
        }

        return certificate;
    }
}
