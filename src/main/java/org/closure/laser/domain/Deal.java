package org.closure.laser.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "deal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "is_cashed")
    private Boolean isCashed;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "to_account")
    private String toAccount;

    @Column(name = "full_weight")
    private Double fullWeight;

    @Column(name = "available_weight")
    private Double availableWeight;

    @Column(name = "arrivel_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivelDate;

    @Column(name = "expected_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedDate;

    @Column(name = "details")
    private String details;

    @JsonIgnoreProperties(value = { "deal", "provider" }, allowSetters = true)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Transaction transaction;

    @OneToMany(mappedBy = "deal", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type", "to", "from", "deal" }, allowSetters = true)
    private Set<Shipment> shipments = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "connection", "tripsDeals", "shipmentDeals", "user" }, allowSetters = true)
    private UserApplication deliver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "connection", "tripsDeals", "shipmentDeals", "user" }, allowSetters = true)
    private UserApplication owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "deals", "to", "from" }, allowSetters = true)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "deals" }, allowSetters = true)
    private DealStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public Deal totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getIsCashed() {
        return this.isCashed;
    }

    public Deal isCashed(Boolean isCashed) {
        this.setIsCashed(isCashed);
        return this;
    }

    public void setIsCashed(Boolean isCashed) {
        this.isCashed = isCashed;
    }

    public String getFromAccount() {
        return this.fromAccount;
    }

    public Deal fromAccount(String fromAccount) {
        this.setFromAccount(fromAccount);
        return this;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return this.toAccount;
    }

    public Deal toAccount(String toAccount) {
        this.setToAccount(toAccount);
        return this;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Double getFullWeight() {
        return this.fullWeight;
    }

    public Deal fullWeight(Double fullWeight) {
        this.setFullWeight(fullWeight);
        return this;
    }

    public void setFullWeight(Double fullWeight) {
        this.fullWeight = fullWeight;
    }

    public Double getAvailableWeight() {
        return this.availableWeight;
    }

    public Deal availableWeight(Double availableWeight) {
        this.setAvailableWeight(availableWeight);
        return this;
    }

    public void setAvailableWeight(Double availableWeight) {
        this.availableWeight = availableWeight;
    }

    public Date getArrivelDate() {
        return this.arrivelDate;
    }

    public Deal arrivelDate(Date arrivelDate) {
        this.setArrivelDate(arrivelDate);
        return this;
    }

    public void setArrivelDate(Date arrivelDate) {
        this.arrivelDate = arrivelDate;
    }

    public Date getExpectedDate() {
        return this.expectedDate;
    }

    public Deal expectedDate(Date expectedDate) {
        this.setExpectedDate(expectedDate);
        return this;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getDetails() {
        return this.details;
    }

    public Deal details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Deal transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    public Set<Shipment> getShipments() {
        return this.shipments;
    }

    public void setShipments(Set<Shipment> shipments) {
        if (this.shipments != null) {
            this.shipments.forEach(i -> i.setDeal(null));
        }
        if (shipments != null) {
            shipments.forEach(i -> i.setDeal(this));
        }
        this.shipments = shipments;
    }

    public Deal shipments(Set<Shipment> shipments) {
        this.setShipments(shipments);
        return this;
    }

    public Deal addShipments(Shipment shipment) {
        this.shipments.add(shipment);
        shipment.setDeal(this);
        return this;
    }

    public Deal removeShipments(Shipment shipment) {
        this.shipments.remove(shipment);
        shipment.setDeal(null);
        return this;
    }

    public UserApplication getDeliver() {
        return this.deliver;
    }

    public void setDeliver(UserApplication userApplication) {
        this.deliver = userApplication;
    }

    public Deal deliver(UserApplication userApplication) {
        this.setDeliver(userApplication);
        return this;
    }

    public UserApplication getOwner() {
        return this.owner;
    }

    public void setOwner(UserApplication userApplication) {
        this.owner = userApplication;
    }

    public Deal owner(UserApplication userApplication) {
        this.setOwner(userApplication);
        return this;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Deal trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    public DealStatus getStatus() {
        return this.status;
    }

    public void setStatus(DealStatus dealStatus) {
        this.status = dealStatus;
    }

    public Deal status(DealStatus dealStatus) {
        this.setStatus(dealStatus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deal)) {
            return false;
        }
        return id != null && id.equals(((Deal) o).id);
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
        return "Deal{" +
                "id=" + getId() +
                ", totalPrice=" + getTotalPrice() +
                ", isCashed='" + getIsCashed() + "'" +
                ", fromAccount='" + getFromAccount() + "'" +
                ", toAccount='" + getToAccount() + "'" +
                ", fullWeight=" + getFullWeight() +
                ", availableWeight=" + getAvailableWeight() +
                ", arrivelDate='" + getArrivelDate() + "'" +
                ", expectedDate='" + getExpectedDate() + "'" +
                ", details='" + getDetails() + "'" +
                "}";
    }
}
