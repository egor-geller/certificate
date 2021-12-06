package com.epam.esm.repository;

import com.epam.esm.repository.builder.SortType;

import java.util.List;
import java.util.Objects;

public class SearchCriteria {

    private List<String> tagList;
    private String certificateName;
    private String certificateDescription;
    private String sortByParameter;
    private SortType orderType;

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
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

    public String getSortByParameter() {
        return sortByParameter;
    }

    public void setSortByParameter(String sortByParameter) {
        this.sortByParameter = sortByParameter;
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
        return tagList.equals(that.tagList) && certificateName.equals(that.certificateName)
                && certificateDescription.equals(that.certificateDescription)
                && sortByParameter.equals(that.sortByParameter) && orderType == that.orderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagList, certificateName, certificateDescription, sortByParameter, orderType);
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "tagList=" + tagList +
                ", certificateName='" + certificateName + '\'' +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", sortByParameter='" + sortByParameter + '\'' +
                ", orderType=" + orderType +
                '}';
    }
}
