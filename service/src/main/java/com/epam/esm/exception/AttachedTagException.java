package com.epam.esm.exception;

public class AttachedTagException extends RuntimeException {

    private final Long tagId;
    private final String tagName;

    public AttachedTagException(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public Long getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }
}
