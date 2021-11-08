package com.epam.esm.dto;

import com.epam.esm.dto.mapper.serializer.DayDurationDeserializer;
import com.epam.esm.dto.mapper.serializer.DayDurationSerializer;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class CertificateDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    @JsonSerialize(using = DayDurationSerializer.class)
    @JsonDeserialize(using = DayDurationDeserializer.class)
    private Duration duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            timezone = "UTC")
    private ZonedDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            timezone = "UTC")
    private ZonedDateTime lastUpdateDate;

    private List<Tag> tagList;

    public CertificateDto() {}

    public CertificateDto(Certificate certificate, List<Tag> tagList) {
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.price = certificate.getPrice();
        this.duration = certificate.getDuration();
        this.createDate = certificate.getCreateDate();
        this.lastUpdateDate = certificate.getLastUpdateDate();
        this.tagList = tagList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CertificateDto that = (CertificateDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(description, that.description) && Objects.equals(price, that.price)
                && Objects.equals(duration, that.duration) && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate) && Objects.equals(tagList, that.tagList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, price, duration, createDate, lastUpdateDate, tagList);
    }

    @Override
    public String toString() {
        return "CertificateDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tagList=" + tagList +
                '}';
    }
}
