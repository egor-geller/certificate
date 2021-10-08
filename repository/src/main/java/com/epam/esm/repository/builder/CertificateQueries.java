package com.epam.esm.repository.builder;

public class CertificateQueries {

    public static final String SELECT_ALL_CERTIFICATES = """
            SELECT cert.id, cert.name, description, price, duration, create_date, last_update_date
            FROM gift_certificate AS cert
            LEFT OUTER JOIN gift_certificate_has_tag gcht
            ON cert.id = gcht.gift_certificate_id
            LEFT OUTER JOIN tag
            ON gcht.tag_id = tag.id
            %s;
            """;

    public static final String SELECT_CERTIFICATE_BY_ID = """
            SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate
            WHERE id = :id;
            """;

    public static final String INSERT_TAG_TO_CERTIFICATE = """
            INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id)
            VALUES (:id, :tag_id);
            """;

    public static final String DELETE_TAG_FROM_CERTIFICATE = """
            DELETE FROM gift_certificate_has_tag
            WHERE gift_certificate_id = :gift_certificate_id AND tag_id = :tag_id;
            """;

    public static final String INSERT_CERTIFICATE = """
            INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
            VALUES (:name, :description, :price, :duration, :create_date, :last_update_date)
            """;

    public static final String UPDATE_CERTIFICATE = """
            UPDATE gift_certificate
            SET name = :name, description = :description, price = :price, duration = :duration, last_update_date = :last_update_date
            WHERE id = :id;
            """;

    public static final String DELETE_CERTIFICATE = """
            DELETE FROM gift_certificate
            WHERE id = :id;
            """;

    private CertificateQueries() {
    }
}
