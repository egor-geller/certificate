package com.epam.esm.dto.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateMapper {

    private CertificateMapper(){}

    public static CertificateDto toCertificateDto(Certificate certificate, List<Tag> tagName) {
        String certificateName = certificate.getName();
        String certificateDescription = certificate.getDescription();
        Double certificatePrice = certificate.getPrice();
        Duration certificateDuration = certificate.getDuration();
        ZonedDateTime certificateCreateDate = certificate.getCreateDate();
        ZonedDateTime certificateLastUpdateDate = certificate.getLastUpdateDate();
        List<String> tagNamesList = tagName.stream().map(Tag::getName).collect(Collectors.toList());

        return new CertificateDto(certificateName, certificateDescription, certificatePrice,
                certificateDuration, certificateCreateDate, certificateLastUpdateDate, tagNamesList);
    }

    public static Certificate fromCertificateDto(CertificateDto certificateDto) {
        Certificate certificate = new Certificate();
        certificate.setName(certificateDto.getName());
        certificate.setDescription(certificateDto.getDescription());
        certificate.setPrice(certificateDto.getPrice());
        certificate.setDuration(certificateDto.getDuration());

        return certificate;
    }
}
