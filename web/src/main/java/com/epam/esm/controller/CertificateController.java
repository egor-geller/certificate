package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Class containing public REST API endpoints related to {@link Certificate} entity.
 *
 * @author Egor Geller
 */
@RestController
@RequestMapping("/api/v1/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    /**
     * Instantiates a new Certificate controller.
     *
     * @param certificateService the certificate service
     */
    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Find certificate by search parameters response entity.
     * All parameters are optional, so if they are not present, all certificates will be retrieved.
     *
     * @param searchCriteria {@link SearchCriteria} instance
     * @return JSON {@link ResponseEntity} object that contains list of {@link CertificateDto}
     */
    @GetMapping
    public ResponseEntity<List<CertificateDto>> findCertificateBySearchingWithCriteria(@ModelAttribute SearchCriteria searchCriteria) {
        List<CertificateDto> certificateDtoList = certificateService.findCertificateByCriteria(searchCriteria);
        return new ResponseEntity<>(certificateDtoList, HttpStatus.OK);
    }

    /**
     * Gets certificate by its unique id.
     *
     * @param id id of the certificate
     * @return JSON {@link ResponseEntity} object that contains {@link CertificateDto} object
     * @throws InvalidEntityException in case when entered id is not a valid one.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") Long id) {
        CertificateDto certificateById = certificateService.findCertificateById(id);
        return new ResponseEntity<>(certificateById, HttpStatus.OK);
    }

    /**
     * Create new {@link Certificate} entity.
     *
     * @param certificateDto {@link CertificateDto} instance
     * @return JSON {@link ResponseEntity} object that contains created {@link CertificateDto} object
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.create(certificateDto);
        return new ResponseEntity<>(certificate, HttpStatus.CREATED);
    }

    /**
     * Update an existing {@link Certificate} entity.
     *
     * @param id             certificate id
     * @param certificateDto {@link CertificateDto} instance
     * @return JSON {@link ResponseEntity} object that contains updated {@link CertificateDto} object
     * @throws InvalidEntityException in case when passed DTO object contains invalid data
     */
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> updateCertificate(@PathVariable("id") Long id,
                                                     @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        CertificateDto update = certificateService.update(certificateDto);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    /**
     * Attach tag to certificate entity.
     *
     * @param tagId  the tag id
     * @param certId the certificate id
     * @return {@code HttpStatus.NO_CONTENT} when entity has been attached
     */
    @PostMapping("/{certId}/tag/{tagId}")
    public ResponseEntity<CertificateDto> attachTagToCertificate(@PathVariable("tagId") Long tagId,
                                                          @PathVariable("certId") Long certId) {

        CertificateDto certificateDto = certificateService.attachTagToCertificate(certId, tagId);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    /**
     * Detach tag from certificate entity.
     *
     * @param tagId  the tag id
     * @param certId the certificate id
     * @return {@code HttpStatus.NO_CONTENT} when entity has been detached
     */
    @DeleteMapping("/{certId}/tag/{tagId}")
    public ResponseEntity<CertificateDto> detachTagFromCertificate(@PathVariable("tagId") Long tagId,
                                                          @PathVariable("certId") Long certId) {

        CertificateDto certificateDto = certificateService.detachTagFromCertificate(certId, tagId);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    /**
     * Delete an existing {@link Certificate} entity.
     *
     * @param id certificate id
     * @return {@code HttpStatus.NO_CONTENT} when entity has been deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCertificate(@PathVariable("id") Long id) {
        certificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
