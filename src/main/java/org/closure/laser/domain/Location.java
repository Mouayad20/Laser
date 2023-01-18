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
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "airport")
    private String airport;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "details")
    private String details;

    @OneToMany(mappedBy = "to", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deals", "to", "from" }, allowSetters = true)
    private Set<Trip> tripDestinations = new HashSet<>();

    @OneToMany(mappedBy = "from", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deals", "to", "from" }, allowSetters = true)
    private Set<Trip> tripSources = new HashSet<>();

    @OneToMany(mappedBy = "to", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type", "to", "from", "deal" }, allowSetters = true)
    private Set<Shipment> shipmentDestinations = new HashSet<>();

    @OneToMany(mappedBy = "from", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type", "to", "from", "deal" }, allowSetters = true)
    private Set<Shipment> shipmentSources = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public Location country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return this.airport;
    }

    public Location airport(String airport) {
        this.setAirport(airport);
        return this;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Location createdAt(Date createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDetails() {
        return this.details;
    }

    public Location details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Set<Trip> getTripDestinations() {
        return this.tripDestinations;
    }

    public void setTripDestinations(Set<Trip> trips) {
        if (this.tripDestinations != null) {
            this.tripDestinations.forEach(i -> i.setTo(null));
        }
        if (trips != null) {
            trips.forEach(i -> i.setTo(this));
        }
        this.tripDestinations = trips;
    }

    public Location tripDestinations(Set<Trip> trips) {
        this.setTripDestinations(trips);
        return this;
    }

    public Location addTripDestinations(Trip trip) {
        this.tripDestinations.add(trip);
        trip.setTo(this);
        return this;
    }

    public Location removeTripDestinations(Trip trip) {
        this.tripDestinations.remove(trip);
        trip.setTo(null);
        return this;
    }

    public Set<Trip> getTripSources() {
        return this.tripSources;
    }

    public void setTripSources(Set<Trip> trips) {
        if (this.tripSources != null) {
            this.tripSources.forEach(i -> i.setFrom(null));
        }
        if (trips != null) {
            trips.forEach(i -> i.setFrom(this));
        }
        this.tripSources = trips;
    }

    public Location tripSources(Set<Trip> trips) {
        this.setTripSources(trips);
        return this;
    }

    public Location addTripSources(Trip trip) {
        this.tripSources.add(trip);
        trip.setFrom(this);
        return this;
    }

    public Location removeTripSources(Trip trip) {
        this.tripSources.remove(trip);
        trip.setFrom(null);
        return this;
    }

    public Set<Shipment> getShipmentDestinations() {
        return this.shipmentDestinations;
    }

    public void setShipmentDestinations(Set<Shipment> shipments) {
        if (this.shipmentDestinations != null) {
            this.shipmentDestinations.forEach(i -> i.setTo(null));
        }
        if (shipments != null) {
            shipments.forEach(i -> i.setTo(this));
        }
        this.shipmentDestinations = shipments;
    }

    public Location shipmentDestinations(Set<Shipment> shipments) {
        this.setShipmentDestinations(shipments);
        return this;
    }

    public Location addShipmentDestinations(Shipment shipment) {
        this.shipmentDestinations.add(shipment);
        shipment.setTo(this);
        return this;
    }

    public Location removeShipmentDestinations(Shipment shipment) {
        this.shipmentDestinations.remove(shipment);
        shipment.setTo(null);
        return this;
    }

    public Set<Shipment> getShipmentSources() {
        return this.shipmentSources;
    }

    public void setShipmentSources(Set<Shipment> shipments) {
        if (this.shipmentSources != null) {
            this.shipmentSources.forEach(i -> i.setFrom(null));
        }
        if (shipments != null) {
            shipments.forEach(i -> i.setFrom(this));
        }
        this.shipmentSources = shipments;
    }

    public Location shipmentSources(Set<Shipment> shipments) {
        this.setShipmentSources(shipments);
        return this;
    }

    public Location addShipmentSources(Shipment shipment) {
        this.shipmentSources.add(shipment);
        shipment.setFrom(this);
        return this;
    }

    public Location removeShipmentSources(Shipment shipment) {
        this.shipmentSources.remove(shipment);
        shipment.setFrom(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            ", airport='" + getAirport() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
