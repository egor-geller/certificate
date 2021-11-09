package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.SearchCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides functionality for manipulating {@link Certificate} entity.
 *
 * @author Geller Egor
 */
@Service
public interface CertificateService extends EntityService<CertificateDto> {

    /**
     * Retrieve certificates according to specified parameters. All parameters are optional
     * and can be used in conjunction, if they are not present, all certificates will be retrieved
     *
     * @param searchCriteria {@link SearchCriteria} class for searching by specific parameter
     * @return list of {@link Certificate}
     */
    List<CertificateDto> findCertificateByCriteria(PaginationContext paginationContext, SearchCriteria searchCriteria);

    /**
     * Find certificate by its id
     *
     * @param id certificate id
     * @return {@link Optional} of {@link Certificate} entity.
     * @throws EntityNotFoundException when there is no such entity
     * @throws InvalidEntityException  when id is not correctly written
     */
    CertificateDto findCertificateById(long id);

    /**
     * Attach tag to existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     * @return {@code true} if {@link Certificate} was attached, otherwise {@code false}
     * @throws InvalidEntityException when id is not correctly written
     */
    CertificateDto attachTagToCertificate(long certificateId, long tagId);

    /**
     * Detach tag from existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     * @return {@code true} if {@link Certificate} was detached, otherwise {@code false}
     * @throws InvalidEntityException when id is not correctly written
     */
    CertificateDto detachTagFromCertificate(PaginationContext paginationContext, long certificateId, long tagId);
}
