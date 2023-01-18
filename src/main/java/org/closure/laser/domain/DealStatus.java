package org.closure.laser.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DealStatus.
 */
@Entity
@Table(name = "deal_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DealStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sequence")
    private Integer sequence;

    @OneToMany(mappedBy = "status", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transaction", "shipments", "deliver", "owner", "trip", "status" }, allowSetters = true)
    private Set<Deal> deals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DealStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DealStatus name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public DealStatus sequence(Integer sequence) {
        this.setSequence(sequence);
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Set<Deal> getDeals() {
        return this.deals;
    }

    public void setDeals(Set<Deal> deals) {
        if (this.deals != null) {
            this.deals.forEach(i -> i.setStatus(null));
        }
        if (deals != null) {
            deals.forEach(i -> i.setStatus(this));
        }
        this.deals = deals;
    }

    public DealStatus deals(Set<Deal> deals) {
        this.setDeals(deals);
        return this;
    }

    public DealStatus addDeals(Deal deal) {
        this.deals.add(deal);
        deal.setStatus(this);
        return this;
    }

    public DealStatus removeDeals(Deal deal) {
        this.deals.remove(deal);
        deal.setStatus(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealStatus)) {
            return false;
        }
        return id != null && id.equals(((DealStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealStatus{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", sequence=" + getSequence() +
                "}";
    }
}
