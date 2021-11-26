package com.epam.esm.repository.query;

public final class CertificateQueries {

    public static final String INSERT_TAG_TO_CERTIFICATE = "INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id) VALUES (?, ?)";

    public static final String DELETE_TAG_FROM_CERTIFICATE = "DELETE FROM gift_certificate_has_tag WHERE gift_certificate_id = ? AND tag_id = ?";

    private CertificateQueries() {
    }
}
