package org.closure.laser.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Constants.
 */
@Entity
@Table(name = "constants")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Constants implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "weight_factor")
    private Double weightFactor;

    @Column(name = "max_weight")
    private Double maxWeight;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Constants id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeightFactor() {
        return this.weightFactor;
    }

    public Constants weightFactor(Double weightFactor) {
        this.setWeightFactor(weightFactor);
        return this;
    }

    public void setWeightFactor(Double weightFactor) {
        this.weightFactor = weightFactor;
    }

    public Double getMaxWeight() {
        return this.maxWeight;
    }

    public Constants maxWeight(Double maxWeight) {
        this.setMaxWeight(maxWeight);
        return this;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Constants)) {
            return false;
        }
        return id != null && id.equals(((Constants) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Constants{" +
            "id=" + getId() +
            ", weightFactor=" + getWeightFactor() +
            ", maxWeight=" + getMaxWeight() +
            "}";
    }
}
