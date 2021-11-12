package com.epam.esm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/generatedata")
public class GenerateController {

    private final GenerateData generateData;

    @Autowired
    public GenerateController(GenerateData generateData) {
        this.generateData = generateData;
    }

    @PostMapping("/user")
    public void generateUsers() {
        generateData.generateUser();
    }

    @PostMapping("/tag")
    public void generateTags() {
        generateData.generateTag();
    }

    @PostMapping("/certificate")
    public void generateCertificates() {
        generateData.generateCertificate();
    }

    @PostMapping("/certificatewithtag")
    public void generateCertificatesWithTags() {
        generateData.generateCertificatesToTags();
    }
}
