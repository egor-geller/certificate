package com.epam.esm.entity;

import com.epam.esm.audit.AuditListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_certificate_price")
@EntityListeners(AuditListener.class)
public class SavedOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;

    @Column(name = "cost")
    private BigDecimal certificateCost;

    public Long getId() {
        return id;
    }

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

    public BigDecimal getCertificateCost() {
        return certificateCost;
    }

    public void setCertificateCost(BigDecimal certificateCost) {
        this.certificateCost = certificateCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedOrder that = (SavedOrder) o;
        return Objects.equals(id, that.id) && Objects.equals(order, that.order)
                && Objects.equals(certificate, that.certificate)
                && Objects.equals(certificateCost, that.certificateCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, certificate, certificateCost);
    }

    @Override
    public String toString() {
        return "SavedOrder{" +
                "id=" + id +
                ", order=" + order +
                ", certificate=" + certificate +
                ", certificateCost=" + certificateCost +
                '}';
    }
}
