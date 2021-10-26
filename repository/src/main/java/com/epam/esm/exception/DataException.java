package com.epam.esm.exception;

public class DataException extends RuntimeException {

    private final Long tagId;
    private final Long certificateId;

    public DataException(Long tagId, Long certificateId) {
        this.tagId = tagId;
        this.certificateId = certificateId;
    }

    public Long getTagId() {
        return tagId;
    }

    public Long getCertificateId() {
        return certificateId;
    }
}
