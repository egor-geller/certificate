package com.epam.esm.repository.query;

public final class TagQueries {

    public static final String SELECT_ALL_TAGS = "SELECT tags FROM Tag tags";

    public static final String SELECT_TAG_BY_NAME = "SELECT tags FROM Tag tags WHERE tags.name = ?1";

    public static final String SELECT_TAG_BY_CERTIFICATE = "SELECT c FROM Certificate t INNER JOIN t.tags c where t.id = ?1";

    private TagQueries(){}
}
