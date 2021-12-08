package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.controller.hateoas.ListHateoasProvider;
import com.epam.esm.controller.hateoas.model.HateoasModel;
import com.epam.esm.controller.hateoas.model.ListHateoasModel;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private final PaginationContext paginationContext;
    private final HateoasProvider<CertificateDto> modelHateoasProvider;
    private final ListHateoasProvider<CertificateDto> listHateoasProvider;


    /**
     * Instantiates a new Certificate controller.
     *
     * @param certificateService   the certificate service
     * @param paginationContext    the pagination context
     * @param modelHateoasProvider the model hateoas provider
     * @param listHateoasProvider  the list hateoas provider
     */
    @Autowired
    public CertificateController(CertificateService certificateService,
                                 PaginationContext paginationContext,
                                 HateoasProvider<CertificateDto> modelHateoasProvider,
                                 ListHateoasProvider<CertificateDto> listHateoasProvider) {
        this.certificateService = certificateService;
        this.paginationContext = paginationContext;
        this.modelHateoasProvider = modelHateoasProvider;
        this.listHateoasProvider = listHateoasProvider;
    }

    /**
     * Find certificate by search parameters response entity.
     * All parameters are optional, so if they are not present, all certificates will be retrieved.
     *
     * @param searchCriteria {@link SearchCriteria} instance
     * @return JSON {@link ResponseEntity} object that contains list of {@link CertificateDto}
     */
    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<ListHateoasModel<CertificateDto>> findCertificateBySearchingWithCriteria(@RequestParam(required = false) Integer page,
                                                                                                   @RequestParam(required = false) Integer pageSize,
                                                                                                   @ModelAttribute SearchCriteria searchCriteria) {
        List<CertificateDto> certificateDtoList = certificateService
                .findCertificateByCriteria(paginationContext.createPagination(page, pageSize), searchCriteria);
        Long count = certificateService.countByCriteria(searchCriteria);
        return createListPagination(certificateDtoList, count);
    }

    /**
     * Gets certificate by its unique id.
     *
     * @param id id of the certificate
     * @return JSON {@link ResponseEntity} object that contains {@link CertificateDto} object
     * @throws InvalidEntityException in case when entered id is not a valid one.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<HateoasModel<CertificateDto>> getCertificateById(@PathVariable("id") Long id) {
        CertificateDto certificateById = certificateService.findCertificateById(id);
        return createModelPagination(certificateById, HttpStatus.OK);
    }

    /**
     * Create new {@link Certificate} entity.
     *
     * @param certificateDto {@link CertificateDto} instance
     * @return JSON {@link ResponseEntity} object that contains created {@link CertificateDto} object
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<HateoasModel<CertificateDto>> createCertificate(@RequestBody CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.create(paginationContext, certificateDto);
        return createModelPagination(certificate, HttpStatus.CREATED);
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
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<HateoasModel<CertificateDto>> updateCertificate(@PathVariable("id") Long id,
                                                                          @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        CertificateDto update = certificateService.update(certificateDto);
        return createModelPagination(update, HttpStatus.OK);
    }

    /**
     * Attach tag to certificate entity.
     *
     * @param tagId  the tag id
     * @param certId the certificate id
     * @return {@code HttpStatus.OK} when entity has been attached
     */
    @PostMapping("/{certId}/tag/{tagId}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<HateoasModel<CertificateDto>> attachTagToCertificate(@PathVariable("tagId") Long tagId,
                                                                               @PathVariable("certId") Long certId) {

        CertificateDto certificateDto = certificateService.attachTagToCertificate(certId, tagId);
        return createModelPagination(certificateDto, HttpStatus.OK);
    }

    /**
     * Detach tag from certificate entity.
     *
     * @param tagId  the tag id
     * @param certId the certificate id
     * @return {@code HttpStatus.OK} when entity has been detached
     */
    @DeleteMapping("/{certId}/tag/{tagId}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<HateoasModel<CertificateDto>> detachTagFromCertificate(@PathVariable("tagId") Long tagId,
                                                                                 @PathVariable("certId") Long certId) {

        CertificateDto certificateDto = certificateService.detachTagFromCertificate(paginationContext, certId, tagId);
        return createModelPagination(certificateDto, HttpStatus.OK);
    }

    /**
     * Delete an existing {@link Certificate} entity.
     *
     * @param id certificate id
     * @return {@code HttpStatus.NO_CONTENT} when entity has been deleted
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<Boolean> deleteCertificate(@PathVariable("id") Long id) {
        certificateService.delete(id, paginationContext);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<HateoasModel<CertificateDto>> createModelPagination(CertificateDto orderDto, HttpStatus status) {
        HateoasModel<CertificateDto> model = new HateoasModel<>(orderDto);
        HateoasModel<CertificateDto> build = model.build(modelHateoasProvider, orderDto, 1L);
        return new ResponseEntity<>(build, status);
    }

    private ResponseEntity<ListHateoasModel<CertificateDto>> createListPagination(List<CertificateDto> orderDtoList,
                                                                                  Long count) {
        ListHateoasModel<CertificateDto> model = new ListHateoasModel<>(orderDtoList);
        ListHateoasModel<CertificateDto> build = model.build(listHateoasProvider, orderDtoList, count);

        return new ResponseEntity<>(build, HttpStatus.OK);
    }
}
