package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<List<CertificateDto>> findCertificateBySearchParameters(@ModelAttribute SearchCriteria searchCriteria) {
        List<CertificateDto> certificateByCriteriaService = certificateService.findCertificateByCriteriaService(searchCriteria);
        return new ResponseEntity<>(certificateByCriteriaService, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") Long id) {
        CertificateDto certificateByIdService = certificateService.findCertificateByIdService(id);
        return new ResponseEntity<>(certificateByIdService, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createCertificate(@RequestBody CertificateDto certificateDto) {
        certificateService.createCertificateService(certificateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> updateCertificate(@PathVariable("id") Long id,
                                                     @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        boolean hasBeenUpdated = certificateService.updateCertificateService(certificateDto);
        return hasBeenUpdated ? new ResponseEntity<>(hasBeenUpdated, HttpStatus.OK)
                : new ResponseEntity<>(hasBeenUpdated, HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteCertificate(@PathVariable("id") Long id) {
        boolean hasBeenDeleted = certificateService.deleteCertificateService(id);
        return hasBeenDeleted ? new ResponseEntity<>(hasBeenDeleted, HttpStatus.OK)
                : new ResponseEntity<>(hasBeenDeleted, HttpStatus.NOT_MODIFIED);
    }
}
