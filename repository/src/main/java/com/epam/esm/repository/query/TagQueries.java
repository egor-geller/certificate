package com.epam.esm.repository.query;

public final class TagQueries {

    public static final String SELECT_ALL_TAGS = "SELECT tags FROM Tag tags";

    public static final String SELECT_TAG_BY_NAME = "SELECT tags FROM Tag tags WHERE tags.name = ?1";

    public static final String SELECT_TAG_BY_CERTIFICATE = "SELECT c FROM Certificate t INNER JOIN t.tags c where t.id = ?1";

    public static final String MOST_WIDELY_USED_TAG = "SELECT t.id, t.name FROM app_user AS u INNER JOIN app_order AS o ON o.id_user = u.id INNER JOIN certificate_order AS co ON co.id_order = o.id INNER JOIN gift_certificate AS c ON c.id = co.id_certificate INNER JOIN certificate_tag AS ct ON ct.id_certificate = c.id INNER JOIN tag AS t ON ct.id_tag = t.id WHERE u.id = (SELECT u.id FROM app_user AS u INNER JOIN app_order AS o ON o.id_user = u.id GROUP BY u.id ORDER BY SUM(o.cost) DESC LIMIT 1) GROUP BY t.id, t.name ORDER BY COUNT(t.name) DESC LIMIT 1;";

    private TagQueries(){}
}
