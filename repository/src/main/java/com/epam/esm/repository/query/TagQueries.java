package com.epam.esm.repository.query;

public final class TagQueries {

    public static final String SELECT_ALL_TAGS = "SELECT tags FROM Tag tags";

    public static final String SELECT_TAG_BY_NAME = "SELECT tags FROM Tag tags WHERE tags.name = ?1";

    public static final String SELECT_TAG_BY_CERTIFICATE = "SELECT c FROM Certificate t INNER JOIN t.tags c where t.id = ?1";

    public static final String MOST_WIDELY_USED_TAG = "SELECT t.id, t.name FROM cert_user AS u INNER JOIN cert_order AS o ON o.cert_user_id = u.id INNER JOIN order_has_certificate AS co ON co.cert_order_id = o.id INNER JOIN gift_certificate AS c ON c.id = co.gift_certificate_id INNER JOIN gift_certificate_has_tag AS ct ON ct.gift_certificate_id = c.id INNER JOIN tag AS t ON ct.tag_id = t.id WHERE u.id = (SELECT u.id FROM cert_user AS u INNER JOIN cert_order AS o ON o.cert_user_id = u.id GROUP BY u.id ORDER BY SUM(o.cost) DESC LIMIT 1) GROUP BY t.id, t.name ORDER BY COUNT(t.name) DESC LIMIT 1;";

    private TagQueries(){}
}
