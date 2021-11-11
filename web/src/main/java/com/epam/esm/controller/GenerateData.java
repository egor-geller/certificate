package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

@Repository
public class GenerateData {

    Faker faker = new Faker(new Locale("en-US"));

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public GenerateData(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void generateUser() {
        List<String> fakeUserList = createFakeUserList();
        fakeUserList.forEach(name -> {
            User user = new User();
            user.setName(name);
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

    //@Transactional
    public void generateCertificate() {
        List<Certificate> fakeCertificateList = createFakeCertificateList();
        fakeCertificateList.forEach(System.out::println);
    }

    private List<String> createFakeUserList() {
        Set<String> fakers = new HashSet<>(1000);
        for (int i = 0; i < 1000; i++) {
            fakers.add(faker.name().firstName());
        }
        return List.copyOf(fakers);
    }

    private List<String> createFakeTagList() {
        Set<String> fakers = new HashSet<>(1000);
        for (int i = 0; i < 1000; i++) {
            fakers.add(faker.name().title());
        }
        return List.copyOf(fakers);
    }

    private List<Certificate> createFakeCertificateList() {
        Set<Certificate> fakers = new HashSet<>(1000);
        for (int i = 0; i < 1000; i++) {
            Certificate certificate = new Certificate();
            certificate.setName(faker.commerce().productName());
            certificate.setDescription(faker.commerce().material());
            certificate.setPrice(BigDecimal.valueOf(Long.parseLong(faker.commerce().price())));
            certificate.setDuration(Duration.ofDays(faker.number().numberBetween(1, 100)));
            fakers.add(certificate);
        }
        return List.copyOf(fakers);
    }
}
