package com.epam.esm.repository.mapper;

import com.epam.esm.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import static java.time.ZoneOffset.UTC;

@Component
public class CertificateMapper implements RowMapper<Certificate> {

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        Certificate certificate = new Certificate();

        certificate.setId(rs.getLong("id"));
        certificate.setName(rs.getString("name"));
        certificate.setDescription(rs.getString("description"));
        certificate.setPrice(rs.getBigDecimal("price"));
        certificate.setDuration(Duration.ofDays(rs.getInt("duration")));
        certificate.setCreateDate(rs.getTimestamp("create_date").toInstant().atZone(UTC));
        certificate.setLastUpdateDate(rs.getTimestamp("last_update_date").toInstant().atZone(UTC));

        return certificate;
    }
}
