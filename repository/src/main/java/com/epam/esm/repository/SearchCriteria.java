package com.epam.esm.repository;

import com.epam.esm.repository.builder.SortType;

import java.util.Objects;

public class SearchCriteria {

    private String tagName;
    private String certificateName;
    private String certificateDescription;
    private SortType sortByName;
    private SortType sortByCreateDate;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    public SortType getSortByName() {
        return sortByName;
    }

    public void setSortByName(SortType sortByName) {
        this.sortByName = sortByName;
    }

    public SortType getSortByCreateDate() {
        return sortByCreateDate;
    }

    public void setSortByCreateDate(SortType sortByCreateDate) {
        this.sortByCreateDate = sortByCreateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchCriteria that = (SearchCriteria) o;
        return tagName.equals(that.tagName) && certificateName.equals(that.certificateName)
                && certificateDescription.equals(that.certificateDescription)
                && sortByName == that.sortByName && sortByCreateDate == that.sortByCreateDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, certificateName, certificateDescription, sortByName, sortByCreateDate);
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "tagName='" + tagName + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", sortByName=" + sortByName +
                ", sortByCreateDate=" + sortByCreateDate +
                '}';
    }
}
