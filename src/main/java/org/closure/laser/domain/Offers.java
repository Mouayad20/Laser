package org.closure.laser.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Offers.
 */
@Entity
@Table(name = "offers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Offers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "shipment_deal_id")
    private Long shipmentDealId;

    @Column(name = "trip_deal_id")
    private Long tripDealId;

    @Column(name = "status")
    private String status;

    @Column(name = "sender_id")
    private Long senderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Offers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShipmentDealId() {
        return this.shipmentDealId;
    }

    public Offers shipmentDealId(Long shipmentDealId) {
        this.setShipmentDealId(shipmentDealId);
        return this;
    }

    public void setShipmentDealId(Long shipmentDealId) {
        this.shipmentDealId = shipmentDealId;
    }

    public Long getTripDealId() {
        return this.tripDealId;
    }

    public Offers tripDealId(Long tripDealId) {
        this.setTripDealId(tripDealId);
        return this;
    }

    public void setTripDealId(Long tripDealId) {
        this.tripDealId = tripDealId;
    }

    public String getStatus() {
        return this.status;
    }

    public Offers status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public Offers senderId(Long senderId) {
        this.setSenderId(senderId);
        return this;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Offers)) {
            return false;
        }
        return id != null && id.equals(((Offers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Offers{" +
            "id=" + getId() +
            ", shipmentDealId=" + getShipmentDealId() +
            ", tripDealId=" + getTripDealId() +
            ", status='" + getStatus() + "'" +
            ", senderId=" + getSenderId() +
            "}";
    }
}
