package com.epam.esm.repository;

import com.epam.esm.repository.builder.SortType;

import java.util.Objects;

public class SearchCriteria {

    private String tagName;
    private String certificateName;
    private String certificateDescription;
    private String sortByNameOrCreationDate;
    private SortType orderType;

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

    public String getSortByNameOrCreationDate() {
        return sortByNameOrCreationDate;
    }

    public void setSortByNameOrCreationDate(String sortByNameOrCreationDate) {
        this.sortByNameOrCreationDate = sortByNameOrCreationDate;
    }

    public SortType getOrderType() {
        return orderType;
    }

    public void setOrderType(SortType orderType) {
        this.orderType = orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchCriteria that = (SearchCriteria) o;
        return tagName.equals(that.tagName) && certificateName.equals(that.certificateName)
                && certificateDescription.equals(that.certificateDescription)
                && sortByNameOrCreationDate.equals(that.sortByNameOrCreationDate) && orderType == that.orderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, certificateName, certificateDescription, sortByNameOrCreationDate, orderType);
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "tagName='" + tagName + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", sortByNameOrCreationDate='" + sortByNameOrCreationDate + '\'' +
                ", orderType=" + orderType +
                '}';
    }
}
