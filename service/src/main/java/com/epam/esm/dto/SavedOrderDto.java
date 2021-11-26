package com.epam.esm.dto;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;

import java.math.BigDecimal;
import java.util.Objects;

public class SavedOrderDto extends IdDto {

    private Long id;
    private Order order;
    private Certificate certificate;
    private BigDecimal cost;

    public SavedOrderDto() {
    }

    public SavedOrderDto(Long id, Order order, Certificate certificate, BigDecimal cost) {
        this.id = id;
        this.order = order;
        this.certificate = certificate;
        this.cost = cost;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedOrderDto that = (SavedOrderDto) o;
        return Objects.equals(id, that.id) && Objects.equals(order, that.order)
                && Objects.equals(certificate, that.certificate) && Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, certificate, cost);
    }

    @Override
    public String toString() {
        return "SavedOrderDto{" +
                "id=" + id +
                ", order=" + order +
                ", certificate=" + certificate +
                ", cost=" + cost +
                '}';
    }
}
