package com.epam.esm.repository.builder;

public final class TagQueries {

    public static final String SELECT_ALL_TAGS = """
            SELECT id, name FROM tag
            """;

    public static final String SELECT_TAG_BY_ID = """
            SELECT id, name FROM tag WHERE id = :id
            """;

    public static final String SELECT_TAG_BY_NAME = """
            SELECT id, name FROM tag WHERE name = :name
            """;

    public static final String SELECT_TAG_BY_CERTIFICATE = """
            SELECT id, name FROM tag INNER JOIN gift_certificate_has_tag AS gt
            ON tag.id = gt.tag_id
            WHERE gt.gift_certificate_id = :certificate_id
            """;

    public static final String INSERT_TAG = """
            INSERT INTO tag (name) VALUES (:name)
            """;

    public static final String DELETE_TAG = """
            DELETE FROM tag WHERE id = :id
            """;

    private TagQueries(){}
}
