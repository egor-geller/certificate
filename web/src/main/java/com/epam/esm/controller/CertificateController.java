package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Class containing public REST API endpoints related to {@link Certificate} entity.
 *
 * @author Egor Geller
 */
@RestController
@RequestMapping("/api/certificates")
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
    public ResponseEntity<List<CertificateDto>> findCertificateBySearchParameters(@ModelAttribute SearchCriteria searchCriteria) {
        List<CertificateDto> certificateByCriteriaService = certificateService.findCertificateByCriteriaService(searchCriteria);
        return new ResponseEntity<>(certificateByCriteriaService, HttpStatus.OK);
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
        CertificateDto certificateByIdService = certificateService.findCertificateByIdService(id);
        return new ResponseEntity<>(certificateByIdService, HttpStatus.OK);
    }

    /**
     * Create new {@link Certificate} entity.
     *
     * @param certificateDto {@link CertificateDto} instance
     * @return JSON {@link ResponseEntity} object that contains created {@link CertificateDto} object
     */
    @PostMapping
    public ResponseEntity<Void> createCertificate(@RequestBody CertificateDto certificateDto) {
        certificateService.createCertificateService(certificateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Update an existing {@link Certificate} entity.
     *
     * @param id             certificate id
     * @param certificateDto {@link CertificateDto} instance
     * @return JSON {@link ResponseEntity} object that contains updated {@link CertificateDto} object
     * @throws InvalidEntityException in case when passed DTO object contains invalid data
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> updateCertificate(@PathVariable("id") Long id,
                                                     @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        boolean hasBeenUpdated = certificateService.updateCertificateService(certificateDto);
        return hasBeenUpdated ? new ResponseEntity<>(hasBeenUpdated, HttpStatus.OK)
                : new ResponseEntity<>(hasBeenUpdated, HttpStatus.NOT_MODIFIED);
    }

    /**
     * Delete an existing {@link Certificate} entity.
     *
     * @param id certificate id
     * @return {@code HttpStatus.OK} and {@code True} when entity has been deleted, otherwise,
     * {@code HttpStatus.NOT_MODIFIED} and {@code False}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCertificate(@PathVariable("id") Long id) {
        boolean hasBeenDeleted = certificateService.deleteCertificateService(id);
        return hasBeenDeleted ? new ResponseEntity<>(hasBeenDeleted, HttpStatus.OK)
                : new ResponseEntity<>(hasBeenDeleted, HttpStatus.NOT_MODIFIED);
    }
}
