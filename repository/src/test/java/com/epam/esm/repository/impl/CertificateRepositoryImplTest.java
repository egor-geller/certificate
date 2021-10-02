package com.epam.esm.repository.impl;

import com.epam.esm.ProfileResolverTest;
import com.epam.esm.config.DatabaseConfiguration;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfiguration.class)
@ActiveProfiles(resolver = ProfileResolverTest.class)
//@Sql("/file.sql)
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository certificate;
    @Autowired
    private TagRepository tag;

    private final Certificate giftCertificate = new Certificate();

    @BeforeEach
    void setUp() {
        giftCertificate.setName("Wonderful");
        giftCertificate.setDescription("10 percent off");
        giftCertificate.setPrice(1.0);
        giftCertificate.setDuration(Duration.ofDays(1));
        giftCertificate.setCreateDate(ZonedDateTime.now(UTC)); //Check for UTC and ZonedDateTime
        giftCertificate.setLastUpdateDate(ZonedDateTime.now(UTC));
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
    void testAttachTag() {
        certificate.attachTag(2, 2);
        List<Tag> tags = tag.findByCertificateId(2L);

        assertEquals(1, tags.size());
    }

    @Test
    void testDetachTag() {
        certificate.detachTag(1, 1);
        List<Tag> tags = tag.findByCertificateId(1L);

        assertTrue(tags.isEmpty());
    }

    @Test
    void testCreate() {

        Optional<Certificate> certificateById = certificate.findById(3);

        assertEquals(certificateById, Optional.empty());

        certificate.create(giftCertificate);

        certificateById = certificate.findById(3);

        assertEquals(certificateById, Optional.of(giftCertificate));
    }

    @Test
    void testUpdate() {
        Optional<Certificate> certificateById = certificate.findById(3);

        certificateById.orElseThrow().setName("Coca-Cola");

        boolean result = certificate.update(certificateById.get());

        assertTrue(result);
    }

    @Test
    void testUpdateNotFound() {
        Optional<Certificate> certificateById = certificate.findById(3);
        certificateById.orElseThrow().setId(0);

        boolean result = certificate.update(certificateById.get());

        assertFalse(result);
    }

    @Test
    void testDelete() {
        boolean result = certificate.delete(1);

        assertTrue(result);
    }

    @Test
    void testDeleteNotFound() {
        boolean result = certificate.delete(0);

        assertFalse(result);
    }
}
