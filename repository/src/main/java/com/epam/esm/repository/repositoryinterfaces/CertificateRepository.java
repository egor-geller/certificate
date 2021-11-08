package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.SearchCriteria;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link Certificate} entity.
 *
 * @author Geller Egor
 */
public interface CertificateRepository extends Repository<Certificate> {

    /**
     * Retrieve certificates according to specified parameters. All parameters are optional
     * and can be used in conjunction, if they are not present, all certificates will be retrieved
     *
     * @param searchCriteria {@link SearchCriteria} entity for searching by specific parameter
     * @return list of {@link Certificate}
     */
    List<Certificate> find(PaginationContext paginationContext, SearchCriteria searchCriteria);

    /**
     * Attach tag to existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     */
    void attachTag(long certificateId, long tagId);

    /**
     * Detach tag from existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     */
    void detachTag(long certificateId, long tagId);

    /**
     * Update an existing certificate.
     *
     * @param certificate {@link Certificate} entity.
     * @return id of {@link Certificate}
     */
    Certificate update(Certificate certificate);
}
