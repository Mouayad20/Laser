package org.closure.laser.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "fly_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date flyTime;

    @Column(name = "arrive_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arriveTime;

    @Column(name = "trip_identifier")
    private String tripIdentifier;

    @Column(name = "details")
    private String details;

    @Column(name = "ticket_image")
    private String ticketImage;

    @Column(name = "trip_type")
    private String tripType;

    @Column(name = "transit")
    private String transit;

    @OneToMany(mappedBy = "trip", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transaction", "shipments", "deliver", "owner", "trip", "status" }, allowSetters = true)
    private Set<Deal> deals = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "tripDestinations", "tripSources", "shipmentDestinations", "shipmentSources" }, allowSetters = true)
    private Location to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "tripDestinations", "tripSources", "shipmentDestinations", "shipmentSources" }, allowSetters = true)
    private Location from;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Trip createdAt(Date createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getFlyTime() {
        return this.flyTime;
    }

    public Trip flyTime(Date flyTime) {
        this.setFlyTime(flyTime);
        return this;
    }

    public void setFlyTime(Date flyTime) {
        this.flyTime = flyTime;
    }

    public Date getArriveTime() {
        return this.arriveTime;
    }

    public Trip arriveTime(Date arriveTime) {
        this.setArriveTime(arriveTime);
        return this;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getTripIdentifier() {
        return this.tripIdentifier;
    }

    public Trip tripIdentifier(String tripIdentifier) {
        this.setTripIdentifier(tripIdentifier);
        return this;
    }

    public void setTripIdentifier(String tripIdentifier) {
        this.tripIdentifier = tripIdentifier;
    }

    public String getDetails() {
        return this.details;
    }

    public Trip details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTicketImage() {
        return this.ticketImage;
    }

    public Trip ticketImage(String ticketImage) {
        this.setTicketImage(ticketImage);
        return this;
    }

    public void setTicketImage(String ticketImage) {
        this.ticketImage = ticketImage;
    }

    public String getTripType() {
        return this.tripType;
    }

    public Trip tripType(String tripType) {
        this.setTripType(tripType);
        return this;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getTransit() {
        return this.transit;
    }

    public Trip transit(String transit) {
        this.setTransit(transit);
        return this;
    }

    public void setTransit(String transit) {
        this.transit = transit;
    }

    public Set<Deal> getDeals() {
        return this.deals;
    }

    public void setDeals(Set<Deal> deals) {
        if (this.deals != null) {
            this.deals.forEach(i -> i.setTrip(null));
        }
        if (deals != null) {
            deals.forEach(i -> i.setTrip(this));
        }
        this.deals = deals;
    }

    public Trip deals(Set<Deal> deals) {
        this.setDeals(deals);
        return this;
    }

    public Trip addDeals(Deal deal) {
        this.deals.add(deal);
        deal.setTrip(this);
        return this;
    }

    public Trip removeDeals(Deal deal) {
        this.deals.remove(deal);
        deal.setTrip(null);
        return this;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location location) {
        this.to = location;
    }

    public Trip to(Location location) {
        this.setTo(location);
        return this;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location location) {
        this.from = location;
    }

    public Trip from(Location location) {
        this.setFrom(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return id != null && id.equals(((Trip) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", flyTime='" + getFlyTime() + "'" +
            ", arriveTime='" + getArriveTime() + "'" +
            ", tripIdentifier='" + getTripIdentifier() + "'" +
            ", details='" + getDetails() + "'" +
            ", ticketImage='" + getTicketImage() + "'" +
            ", tripType='" + getTripType() + "'" +
            ", transit='" + getTransit() + "'" +
            "}";
    }
}
