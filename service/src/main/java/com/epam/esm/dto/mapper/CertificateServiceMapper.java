package com.epam.esm.dto.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateServiceMapper {

    private CertificateServiceMapper() {
    }

    public CertificateDto changeCertificateToDto(Certificate certificate, List<Tag> tagName) {
        List<String> tagNamesList = tagName.stream().map(Tag::getName).collect(Collectors.toList());

        return new CertificateDto(certificate, tagNamesList);
    }

    public Certificate changeCertificateFromDto(CertificateDto certificateDto) {
        Certificate certificate = new Certificate();
        certificate.setName(certificateDto.getName());
        certificate.setDescription(certificateDto.getDescription());
        certificate.setPrice(certificateDto.getPrice());
        certificate.setDuration(certificateDto.getDuration());
        certificate.setCreateDate(certificateDto.getCreateDate());
        certificate.setLastUpdateDate(certificateDto.getLastUpdateDate());

        return certificate;
    }
}
