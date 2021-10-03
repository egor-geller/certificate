package com.epam.esm.repository.impl;

import com.epam.esm.ProfileResolverTest;
import com.epam.esm.config.DatabaseConfiguration;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.SortType;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfiguration.class)
@ActiveProfiles(resolver = ProfileResolverTest.class)
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository certificate;
    @Autowired
    private TagRepository tag;

    private final Certificate giftCertificate = new Certificate();

    String tagName = "tag1";
    String certificateName = "name1";
    String certificateDescription = "Description1";

    @BeforeEach
    void setUp() {
        giftCertificate.setName("Wonderful");
        giftCertificate.setDescription("10 percent off");
        giftCertificate.setPrice(1.0);
        giftCertificate.setDuration(Duration.ofDays(1));
        giftCertificate.setCreateDate(ZonedDateTime.now(UTC));
        giftCertificate.setLastUpdateDate(ZonedDateTime.now(UTC));
    }

    @Test
    void findByTagNameParamTest() {
        List<Certificate> certificateList =
                certificate.find(tagName, null, null, null, null);

        boolean allMatch = certificateList.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && certificateList.size() == 1);
    }

    @Test
    void findByNameParamTest() {
        List<Certificate> certificateList =
                certificate.find(null, certificateName, null, null, null);

        boolean allMatch = certificateList.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && certificateList.size() == 1);
    }

    @Test
    void findByDescriptionParamTest() {
        List<Certificate> certificateList =
                certificate.find(null, null, certificateDescription, null, null);

        boolean allMatch = certificateList.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && certificateList.size() == 1);
    }

    @Test
    void sortByNameASCTest() {
        List<Certificate> certificateList =
                certificate.find(null, null, null, SortType.ASC, null);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getName))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByNameDESCTest() {
        List<Certificate> certificateList =
                certificate.find(null, null, null, SortType.DESC, null);

        List<Certificate> actual = certificateList.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Certificate::getName)))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByCreateDateASCTest() {
        List<Certificate> certificateList =
                certificate.find(null, null, null, null, SortType.ASC);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getCreateDate))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByCreateDateDESCTest() {
        List<Certificate> certificateList =
                certificate.find(null, null, null, null, SortType.DESC);

        List<Certificate> actual = certificateList.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Certificate::getCreateDate)))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void findByIdTest() {
        Optional<Certificate> cert = certificate.findById(1);
        assertTrue(cert.isPresent() && cert.get().getId() == 1);
    }

    @Test
    void notFoundByIdTest() {
        Optional<Certificate> cert = certificate.findById(0);
        assertTrue(cert.isEmpty());
    }

    @Test
    void attachTagTest() {
        certificate.attachTag(2, 2);
        List<Tag> tags = tag.findByCertificateId(2L);

        assertEquals(1, tags.size());
    }

    @Test
    void detachTagTest() {
        certificate.detachTag(1, 1);
        List<Tag> tags = tag.findByCertificateId(1L);

        assertTrue(tags.isEmpty());
    }

    @Test
    void createTest() {
        Optional<Certificate> certificateById = certificate.findById(3);

        assertEquals(certificateById, Optional.empty());

        certificate.create(giftCertificate);

        certificateById = certificate.findById(3);

        assertEquals(certificateById, Optional.of(giftCertificate));
    }

    @Test
    void updateTest() {
        Optional<Certificate> certificateById = certificate.findById(3);

        certificateById.orElseThrow().setName("Coca-Cola");

        boolean result = certificate.update(certificateById.get());

        assertTrue(result);
    }

    @Test
    void updateNotFoundTest() {
        Optional<Certificate> certificateById = certificate.findById(3);
        certificateById.orElseThrow().setId(0);

        boolean result = certificate.update(certificateById.get());

        assertFalse(result);
    }

    @Test
    void deleteTest() {
        boolean result = certificate.delete(1);

        assertTrue(result);
    }

    @Test
    void deleteNotFoundTest() {
        boolean result = certificate.delete(0);

        assertFalse(result);
    }
}
