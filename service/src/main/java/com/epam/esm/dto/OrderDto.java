package com.epam.esm.dto;

import com.epam.esm.entity.Certificate;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class OrderDto extends RepresentationModel<OrderDto> {

    private Long id;
    private Long userId;
    private List<Certificate> certificateList;
    private BigDecimal cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            timezone = "UTC")
    private ZonedDateTime purchaseDate;

    public OrderDto() {
    }

    public OrderDto(Long id, Long userId, List<Certificate> certificateList, BigDecimal cost, ZonedDateTime purchaseDate) {
        this.id = id;
        this.userId = userId;
        this.certificateList = certificateList;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Certificate> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<Certificate> certificateList) {
        this.certificateList = certificateList;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ZonedDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) && Objects.equals(userId, orderDto.userId)
                && Objects.equals(certificateList, orderDto.certificateList) && Objects.equals(cost, orderDto.cost)
                && Objects.equals(purchaseDate, orderDto.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, certificateList, cost, purchaseDate);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", certificateList=" + certificateList +
                ", cost=" + cost +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}
