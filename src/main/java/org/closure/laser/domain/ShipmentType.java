package org.closure.laser.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShipmentType.
 */
@Entity
@Table(name = "shipment_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShipmentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "factor")
    private Double factor;

    @OneToMany(mappedBy = "type")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type", "to", "from", "deal" }, allowSetters = true)
    private Set<Shipment> shipments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShipmentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ShipmentType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFactor() {
        return this.factor;
    }

    public ShipmentType factor(Double factor) {
        this.setFactor(factor);
        return this;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Set<Shipment> getShipments() {
        return this.shipments;
    }

    public void setShipments(Set<Shipment> shipments) {
        if (this.shipments != null) {
            this.shipments.forEach(i -> i.setType(null));
        }
        if (shipments != null) {
            shipments.forEach(i -> i.setType(this));
        }
        this.shipments = shipments;
    }

    public ShipmentType shipments(Set<Shipment> shipments) {
        this.setShipments(shipments);
        return this;
    }

    public ShipmentType addShipments(Shipment shipment) {
        this.shipments.add(shipment);
        shipment.setType(this);
        return this;
    }

    public ShipmentType removeShipments(Shipment shipment) {
        this.shipments.remove(shipment);
        shipment.setType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentType)) {
            return false;
        }
        return id != null && id.equals(((ShipmentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", factor=" + getFactor() +
            "}";
    }
}
