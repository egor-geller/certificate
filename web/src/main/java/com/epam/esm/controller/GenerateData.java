package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

@Repository
public class GenerateData {

    Faker faker = new Faker(new Locale("en-US"));
    Random random = new Random();

    @PersistenceContext
    private EntityManager entityManager;
    private final CertificateServiceImpl repository;

    @Autowired
    public GenerateData(EntityManager entityManager, CertificateServiceImpl repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    @Transactional
    public void generateUser() {
        List<String> fakeUserList = createFakeUserList();
        fakeUserList.forEach(name -> {
            User user = new User();
            user.setUsername(name);
            user.setPassword(faker.internet().password(6, 20, true, true));
            user.setRole(Role.USER);
            entityManager.persist(user);
        });
    }

    @Transactional
    public void generateTag() {
        List<String> fakeTagList = createFakeTagList();
        fakeTagList.forEach(fakeTag -> {
            Tag tag = new Tag();
            tag.setName(fakeTag);
            entityManager.persist(tag);
        });
    }

    @Transactional
    public void generateCertificate() {
        Set<Certificate> fakeCertificateList = new HashSet<>(10000);
        int count = 0;
        while (fakeCertificateList.size() < 10000) {
            Certificate certificate = new Certificate();
            certificate.setName(faker.name().firstName());
            certificate.setDescription(faker.commerce().productName());
            certificate.setPrice(BigDecimal.valueOf((random.nextInt(100 - 1) + (long) 1)));
            certificate.setDuration(Duration.ofDays((random.nextInt(100 - 1) + (long) 1)));
            fakeCertificateList.add(certificate);
        }

        for (Certificate certificate : fakeCertificateList) {
            entityManager.persist(certificate);
            entityManager.flush();
            long tagNumber = (long) (Math.random() * (1038 - 39)) + 39;
            repository.attachTagToCertificate(certificate.getId(), tagNumber);
            count++;
            if ((count % 30) == 0) { //Done for greater performance
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    private List<String> createFakeUserList() {
        Set<String> fakers = new HashSet<>(1000);
        while (fakers.size() < 1000) {
            fakers.add(faker.name().firstName());
        }
        return List.copyOf(fakers);
    }

    private List<String> createFakeTagList() {
        Set<String> fakers = new HashSet<>(1000);
        while (fakers.size() < 1000) {
            fakers.add(faker.name().title());
        }
        return List.copyOf(fakers);
    }
}
