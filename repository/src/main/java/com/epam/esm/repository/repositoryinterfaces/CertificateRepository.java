package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.SearchCriteria;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link Certificate} com.epam.esm.entity.
 *
 * @author Geller Egor
 */
public interface CertificateRepository extends Repository<Certificate> {

    /**
     * Retrieve certificates according to specified parameters. All parameters are optional
     * and can be used in conjunction, if they are not present, all certificates will be retrieved
     *
     * @param searchCriteria {@link SearchCriteria} com.epam.esm.entity for searching by specific parameter
     * @return list of {@link Certificate}
     */
    List<Certificate> find(SearchCriteria searchCriteria);

    /**
     * Attach tag to existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     * @return {@code true} if {@link Certificate} was attached, otherwise {@code false}
     */
    boolean attachTag(long certificateId, long tagId);

    /**
     * Detach tag from existing certificate.
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     * @return {@code true} if {@link Certificate} was detached, otherwise {@code false}
     */
    boolean detachTag(long certificateId, long tagId);

    /**
     * Update an existing certificate.
     *
     * @param certificate {@link Certificate} com.epam.esm.entity.
     * @return {@code true} if {@link Certificate} was updated, otherwise {@code false}
     */
    boolean update(Certificate certificate);
}
