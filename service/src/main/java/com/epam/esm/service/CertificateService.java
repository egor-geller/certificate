package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.SortType;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides functionality for manipulating {@link Certificate} entity.
 *
 * @author Geller Egor
 */
public interface CertificateService {
    /**
     * Retrieve certificates according to specified parameters. All parameters are optional
     * and can be used in conjunction, if they are not present, all certificates will be retrieved
     *
     * @param tagName                name of the tag
     * @param certificateName        certificate name (part-qualified)
     * @param certificateDescription certificate description (part-qualified)
     * @param sortByName             sort names in ASC/DESC order
     * @param sortByCreateDate       sort creation date in ASC/DESC order
     * @return list of {@link Certificate}
     */
    List<CertificateDto> findCertificateByService(String tagName, String certificateName, String certificateDescription,
                                                  SortType sortByName, SortType sortByCreateDate);

    /**
     * Find certificate by its id
     *
     * @param id certificate id
     * @return {@link Optional} of {@link Certificate} entity.
     */
    CertificateDto findCertificateByIdService(long id);

    /**
     * Attach tag to existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     * @return {@code true} if {@link Certificate} was attached, otherwise {@code false}
     */
    boolean attachTagToCertificateService(long certificateId, long tagId);

    /**
     * Detach tag from existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     * @return {@code true} if {@link Certificate} was detached, otherwise {@code false}
     */
    boolean detachTagFromCertificateService(long certificateId, long tagId);

    /**
     * Create a new certificate.
     *
     * @param certificate {@link Certificate} instance
     */
    void createCertificateService(CertificateDto certificate);

    /**
     * Update an existing certificate.
     *
     * @param certificate {@link Certificate} entity.
     * @return {@code true} if {@link Certificate} was updated, otherwise {@code false}
     */
    boolean updateCertificateService(CertificateDto certificate);

    /**
     * Delete an existing certificate.
     *
     * @param id certificate id
     * @return {@code true} if {@link Certificate} was deleted, otherwise {@code false}
     */
    boolean deleteCertificateService(long id);
}
