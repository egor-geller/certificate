package com.epam.esm.dto.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateMapper {

    public CertificateMapper() {
    }

    public List<CertificateDto> changeListOfCertificatesToDto(List<Certificate> certificates, TagRepository tagRepository) {
        List<CertificateDto> certificateDtoList = new ArrayList<>();

        for (Certificate certificate : certificates) {
            long id = certificate.getId();
            List<Tag> byCertificateId = tagRepository.findByCertificateId(id);
            List<String> tagNamesList = byCertificateId.stream().map(Tag::getName).collect(Collectors.toList());
            certificateDtoList.add(new CertificateDto(certificate, tagNamesList));
        }

        return certificateDtoList;
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

        return certificate;
    }
}
