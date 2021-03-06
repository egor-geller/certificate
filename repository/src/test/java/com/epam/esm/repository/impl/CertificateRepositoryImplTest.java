package com.epam.esm.repository.impl;

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

@Transactional
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository certificate;
    @Autowired
    private TagRepository tag;

    private final SearchCriteria searchCriteria = new SearchCriteria();

    private final Certificate giftCertificate = new Certificate();

    String tagName = "tag1";
    String certificateName = "certificate1";
    String certificateDescription = "description1";


    @BeforeEach
    void setUp() {
        giftCertificate.setId(26);
        giftCertificate.setName("Wonderful");
        giftCertificate.setDescription("10 percent off");
        giftCertificate.setPrice(BigDecimal.ONE);
        giftCertificate.setDuration(Duration.ofDays(1));
        giftCertificate.setCreateDate(ZonedDateTime.now(UTC));
        giftCertificate.setLastUpdateDate(ZonedDateTime.now(UTC));
    }

   /* @Test
    void findByTagNameParamTest() {
        searchCriteria.setTagName(tagName);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        boolean allMatch = certificateList.stream()
                .anyMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription)
                );

        assertTrue(allMatch && certificateList.size() > 0);
    }

    @Test
    void findByNameParamTest() {
        searchCriteria.setCertificateName(certificateName);
        List<Certificate> certificateList = certificate.find(searchCriteria);
        List<Certificate> list = certificateList.stream().distinct().collect(Collectors.toList());

        boolean allMatch = list.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && list.size() == 1);
    }

    @Test
    void findByDescriptionParamTest() {
        searchCriteria.setCertificateDescription(certificateDescription);
        List<Certificate> certificateList = certificate.find(searchCriteria);
        List<Certificate> list = certificateList.stream().distinct().collect(Collectors.toList());
        boolean allMatch = list.stream()
                .allMatch(certificate1 -> certificate1.getName().equals(certificateName)
                        && certificate1.getDescription().equals(certificateDescription));

        assertTrue(allMatch && list.size() == 1);
    }

    @Test
    void sortByNameAscTest() {
        searchCriteria.setSortByNameOrCreationDate("name");
        searchCriteria.setOrderType(SortType.ASC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Certificate::getName)))
                .collect(Collectors.toList());

        assertEquals(certificateList.get(certificateList.size() - 1), actual.get(actual.size() -1));
    }

    @Test
    void sortByNameDescTest() {
        searchCriteria.setSortByNameOrCreationDate("name");
        searchCriteria.setOrderType(SortType.DESC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getName))
                .collect(Collectors.toList());

        assertEquals(certificateList.get(0), actual.get(0));
    }

    @Test
    void sortByCreateDateAscTest() {
        searchCriteria.setSortByNameOrCreationDate("create_date");
        searchCriteria.setOrderType(SortType.ASC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getCreateDate))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void sortByCreateDateDescTest() {
        searchCriteria.setSortByNameOrCreationDate("create_date");
        searchCriteria.setOrderType(SortType.DESC);
        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Certificate::getCreateDate)))
                .collect(Collectors.toList());

        assertEquals(certificateList, actual);
    }

    @Test
    void findByNameAndSortAscTest() {
        searchCriteria.setTagName(tagName);
        searchCriteria.setCertificateName(certificateName);
        searchCriteria.setCertificateDescription(certificateDescription);
        searchCriteria.setSortByNameOrCreationDate("name");
        searchCriteria.setOrderType(SortType.ASC);

        List<Certificate> certificateList = certificate.find(searchCriteria);

        List<Certificate> actual = certificateList.stream()
                .sorted(Comparator.comparing(Certificate::getName))
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
    void detachTagTest() {
        certificate.detachTag(25, 6);
        List<Tag> tags = tag.findByCertificateId(25L);
        tags.forEach(tag -> assertNotEquals(6, tag.getId()));
    }

    @Test
    void attachTagTest() {
        certificate.attachTag(25, 14);
        List<Tag> tags = tag.findByCertificateId(25L);
        System.out.println(tags);
        tags.forEach(tag -> {
            if (tag.getId() == 14) {
                assertEquals(14, tag.getId());
            }
        });
    }

    @Test
    void createTest() {
        Optional<Certificate> certificateById = certificate.findById(0L);

        assertEquals(certificateById, Optional.empty());

        certificate.create(giftCertificate);

        Optional<Certificate> certificateById1 = certificate.findById(26L);

        assertEquals(certificateById1.get().getId(), Optional.of(giftCertificate).get().getId());
    }

    @Test
    void updateTest() {
        Optional<Certificate> certificateById = certificate.findById(25L);

        certificateById.orElseThrow().setName("Coca-Cola");

        Long update = certificate.update(certificateById.get());

        assertEquals(25L, update);
    }

    @Test
    void deleteTest() {
        boolean result = certificate.delete(32L);

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
        searchCriteria.setSortByNameOrCreationDate("name");
        searchCriteria.setOrderType(SortType.ASC);

        map.put(tagName, tagName);
        map.put(certificateName, certificateName);
        map.put(certificateDescription, certificateDescription);
        map.put("sortByName", searchCriteria.getSortByNameOrCreationDate());
        map.put("orderType", SortType.ASC.name());

        return searchCriteria;
    }

    private SearchCriteria criteriaToSearchWithDESC() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setTagName(tagName);
        searchCriteria.setCertificateName(certificateName);
        searchCriteria.setCertificateDescription(certificateDescription);
        searchCriteria.setSortByNameOrCreationDate("name");
        searchCriteria.setOrderType(SortType.DESC);
        return searchCriteria;
    }*/
}
