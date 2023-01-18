package org.closure.laser.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Task entity.\n@author The JHipster team.
 */
@Schema(description = "Task entity.\n@author The JHipster team.")
@Entity
@Table(name = "shipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Shipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "price")
    private Double price;

    @Column(name = "details")
    private String details;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "shipments" }, allowSetters = true)
    private ShipmentType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "tripDestinations", "tripSources", "shipmentDestinations", "shipmentSources" }, allowSetters = true)
    private Location to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "tripDestinations", "tripSources", "shipmentDestinations", "shipmentSources" }, allowSetters = true)
    private Location from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "transaction", "shipments", "deliver", "owner", "trip", "status" }, allowSetters = true)
    private Deal deal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shipment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return this.weight;
    }

    public Shipment weight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getUrl() {
        return this.url;
    }

    public Shipment url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public Shipment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Shipment createdAt(Date createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public Shipment imgUrl(String imgUrl) {
        this.setImgUrl(imgUrl);
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getCost() {
        return this.cost;
    }

    public Shipment cost(Double cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getPrice() {
        return this.price;
    }

    public Shipment price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDetails() {
        return this.details;
    }

    public Shipment details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ShipmentType getType() {
        return this.type;
    }

    public void setType(ShipmentType shipmentType) {
        this.type = shipmentType;
    }

    public Shipment type(ShipmentType shipmentType) {
        this.setType(shipmentType);
        return this;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location location) {
        this.to = location;
    }

    public Shipment to(Location location) {
        this.setTo(location);
        return this;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location location) {
        this.from = location;
    }

    public Shipment from(Location location) {
        this.setFrom(location);
        return this;
    }

    public Deal getDeal() {
        return this.deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public Shipment deal(Deal deal) {
        this.setDeal(deal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shipment)) {
            return false;
        }
        return id != null && id.equals(((Shipment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shipment{" +
            "id=" + getId() +
            ", weight=" + getWeight() +
            ", url='" + getUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", cost=" + getCost() +
            ", price=" + getPrice() +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
