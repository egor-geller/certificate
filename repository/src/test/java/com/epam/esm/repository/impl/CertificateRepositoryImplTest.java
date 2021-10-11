package com.epam.esm.repository.impl;

import com.epam.esm.ProfileResolverTest;
import com.epam.esm.config.DatabaseConfiguration;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.builder.SortType;
import com.epam.esm.repository.repositoryinterfaces.CertificateRepository;
import com.epam.esm.repository.repositoryinterfaces.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfiguration.class)
@Transactional
@ActiveProfiles(resolver = ProfileResolverTest.class)
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository certificate;
    @Autowired
    private TagRepository tag;

    private final SearchCriteria searchCriteria = new SearchCriteria();

    private final Certificate giftCertificate = new Certificate();

    String tagName = "tag1";
    String certificateName = "name1";
    String certificateDescription = "description1";


    @BeforeEach
    void setUp() {
        giftCertificate.setName("Wonderful");
        giftCertificate.setDescription("10 percent off");
        giftCertificate.setPrice(BigDecimal.ONE);
        giftCertificate.setDuration(Duration.ofDays(1));
        giftCertificate.setCreateDate(ZonedDateTime.now(UTC));
        giftCertificate.setLastUpdateDate(ZonedDateTime.now(UTC));
    }

    @Test
    void findByTagNameParamTest() {
        searchCriteria.setTagName(tagName);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        boolean allMatch = certificateList.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && certificateList.size() == 1);
    }

    @Test
    void findByNameParamTest() {
        searchCriteria.setCertificateName(certificateName);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        boolean allMatch = certificateList.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && certificateList.size() == 1);
    }

    @Test
    void findByDescriptionParamTest() {
        searchCriteria.setCertificateDescription(certificateDescription);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        boolean allMatch = certificateList.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && certificateList.size() == 1);
    }

    @Test
    void sortByNameAscTest() {
        searchCriteria.setSortByName(SortType.ASC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getName))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByNameDescTest() {
        searchCriteria.setSortByName(SortType.DESC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Certificate::getName)))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByCreateDateAscTest() {
        searchCriteria.setSortByCreateDate(SortType.ASC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getCreateDate))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByCreateDateDescTest() {
        searchCriteria.setSortByCreateDate(SortType.DESC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Certificate::getCreateDate)))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByCreateDateAscAndNameInDescTest() {
        searchCriteria.setSortByName(SortType.DESC);
        searchCriteria.setSortByCreateDate(SortType.ASC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getCreateDate))
                .sorted(Collections.reverseOrder(Comparator.comparing(Certificate::getName)))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void findByIdTest() {
        Optional<Certificate> cert = certificate.findById(1L);
        assertTrue(cert.isPresent() && cert.get().getId() == 1);
    }

    @Test
    void notFoundByIdTest() {
        Optional<Certificate> cert = certificate.findById(0L);
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
        Optional<Certificate> certificateById = certificate.findById(5L);

        assertEquals(certificateById, Optional.empty());

        certificate.create(giftCertificate);

        certificateById = certificate.findById(5L);

        assertEquals(certificateById, Optional.of(giftCertificate));
    }

    @Test
    void updateTest() {
        Optional<Certificate> certificateById = certificate.findById(3L);

        certificateById.orElseThrow().setName("Coca-Cola");

        boolean result = certificate.update(certificateById.get());

        assertTrue(result);
    }

    @Test
    void updateNotFoundTest() {
        Optional<Certificate> certificateById = certificate.findById(3L);
        certificateById.orElseThrow().setId(0);

        boolean result = certificate.update(certificateById.get());

        assertFalse(result);
    }

    @Test
    void deleteTest() {
        boolean result = certificate.delete(1L);

        assertTrue(result);
    }

    @Test
    void deleteNotFoundTest() {
        boolean result = certificate.delete(0L);

        assertFalse(result);
    }

    private SearchCriteria criteriaToSearchWithASC() {
        Map<String, String> map = new HashMap<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setTagName(tagName);
        searchCriteria.setCertificateName(certificateName);
        searchCriteria.setCertificateDescription(certificateDescription);
        searchCriteria.setSortByName(SortType.ASC);
        searchCriteria.setSortByCreateDate(SortType.ASC);

        map.put(tagName, tagName);
        map.put(certificateName, certificateName);
        map.put(certificateDescription, certificateDescription);
        map.put("sortByNameASC", SortType.ASC.name());
        map.put("sortByCreateDateASC", SortType.ASC.name());

        return searchCriteria;
    }

    private SearchCriteria criteriaToSearchWithDESC() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setTagName(tagName);
        searchCriteria.setCertificateName(certificateName);
        searchCriteria.setCertificateDescription(certificateDescription);
        searchCriteria.setSortByName(SortType.DESC);
        searchCriteria.setSortByCreateDate(SortType.DESC);
        return searchCriteria;
    }
}
