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
 * A UserApplication.
 */
@Entity
@Table(name = "user_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "passport")
    private String passport;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "is_google_account")
    private Boolean isGoogleAccount;

    @Column(name = "is_facebook_account")
    private Boolean isFacebookAccount;

    @Column(name = "is_twitter_account")
    private Boolean isTwitterAccount;

    @Column(name = "image")
    private String image;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "five_star")
    private Double fiveStar;

    @Column(name = "four_satr")
    private Double fourSatr;

    @Column(name = "three_star")
    private Double threeStar;

    @Column(name = "two_star")
    private Double twoStar;

    @Column(name = "one_star")
    private Double oneStar;

    @Column(name = "detalis")
    private String detalis;

    @JsonIgnoreProperties(value = { "userApplication" }, allowSetters = true)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Connection connection;

    @OneToMany(mappedBy = "deliver", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transaction", "shipments", "deliver", "owner", "trip", "status" }, allowSetters = true)
    private Set<Deal> tripsDeals = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transaction", "shipments", "deliver", "owner", "trip", "status" }, allowSetters = true)
    private Set<Deal> shipmentDeals = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserApplication phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassport() {
        return this.passport;
    }

    public UserApplication passport(String passport) {
        this.setPassport(passport);
        return this;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public UserApplication createdAt(Date createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsGoogleAccount() {
        return this.isGoogleAccount;
    }

    public UserApplication isGoogleAccount(Boolean isGoogleAccount) {
        this.setIsGoogleAccount(isGoogleAccount);
        return this;
    }

    public void setIsGoogleAccount(Boolean isGoogleAccount) {
        this.isGoogleAccount = isGoogleAccount;
    }

    public Boolean getIsFacebookAccount() {
        return this.isFacebookAccount;
    }

    public UserApplication isFacebookAccount(Boolean isFacebookAccount) {
        this.setIsFacebookAccount(isFacebookAccount);
        return this;
    }

    public void setIsFacebookAccount(Boolean isFacebookAccount) {
        this.isFacebookAccount = isFacebookAccount;
    }

    public Boolean getIsTwitterAccount() {
        return this.isTwitterAccount;
    }

    public UserApplication isTwitterAccount(Boolean isTwitterAccount) {
        this.setIsTwitterAccount(isTwitterAccount);
        return this;
    }

    public void setIsTwitterAccount(Boolean isTwitterAccount) {
        this.isTwitterAccount = isTwitterAccount;
    }

    public String getImage() {
        return this.image;
    }

    public UserApplication image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getRate() {
        return this.rate;
    }

    public UserApplication rate(Double rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getFiveStar() {
        return this.fiveStar;
    }

    public UserApplication fiveStar(Double fiveStar) {
        this.setFiveStar(fiveStar);
        return this;
    }

    public void setFiveStar(Double fiveStar) {
        this.fiveStar = fiveStar;
    }

    public Double getFourSatr() {
        return this.fourSatr;
    }

    public UserApplication fourSatr(Double fourSatr) {
        this.setFourSatr(fourSatr);
        return this;
    }

    public void setFourSatr(Double fourSatr) {
        this.fourSatr = fourSatr;
    }

    public Double getThreeStar() {
        return this.threeStar;
    }

    public UserApplication threeStar(Double threeStar) {
        this.setThreeStar(threeStar);
        return this;
    }

    public void setThreeStar(Double threeStar) {
        this.threeStar = threeStar;
    }

    public Double getTwoStar() {
        return this.twoStar;
    }

    public UserApplication twoStar(Double twoStar) {
        this.setTwoStar(twoStar);
        return this;
    }

    public void setTwoStar(Double twoStar) {
        this.twoStar = twoStar;
    }

    public Double getOneStar() {
        return this.oneStar;
    }

    public UserApplication oneStar(Double oneStar) {
        this.setOneStar(oneStar);
        return this;
    }

    public void setOneStar(Double oneStar) {
        this.oneStar = oneStar;
    }

    public String getDetalis() {
        return this.detalis;
    }

    public UserApplication detalis(String detalis) {
        this.setDetalis(detalis);
        return this;
    }

    public void setDetalis(String detalis) {
        this.detalis = detalis;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public UserApplication connection(Connection connection) {
        this.setConnection(connection);
        return this;
    }

    public Set<Deal> getTripsDeals() {
        return this.tripsDeals;
    }

    public void setTripsDeals(Set<Deal> deals) {
        if (this.tripsDeals != null) {
            this.tripsDeals.forEach(i -> i.setDeliver(null));
        }
        if (deals != null) {
            deals.forEach(i -> i.setDeliver(this));
        }
        this.tripsDeals = deals;
    }

    public UserApplication tripsDeals(Set<Deal> deals) {
        this.setTripsDeals(deals);
        return this;
    }

    public UserApplication addTripsDeals(Deal deal) {
        this.tripsDeals.add(deal);
        deal.setDeliver(this);
        return this;
    }

    public UserApplication removeTripsDeals(Deal deal) {
        this.tripsDeals.remove(deal);
        deal.setDeliver(null);
        return this;
    }

    public Set<Deal> getShipmentDeals() {
        return this.shipmentDeals;
    }

    public void setShipmentDeals(Set<Deal> deals) {
        if (this.shipmentDeals != null) {
            this.shipmentDeals.forEach(i -> i.setOwner(null));
        }
        if (deals != null) {
            deals.forEach(i -> i.setOwner(this));
        }
        this.shipmentDeals = deals;
    }

    public UserApplication shipmentDeals(Set<Deal> deals) {
        this.setShipmentDeals(deals);
        return this;
    }

    public UserApplication addShipmentDeals(Deal deal) {
        this.shipmentDeals.add(deal);
        deal.setOwner(this);
        return this;
    }

    public UserApplication removeShipmentDeals(Deal deal) {
        this.shipmentDeals.remove(deal);
        deal.setOwner(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserApplication user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserApplication)) {
            return false;
        }
        return id != null && id.equals(((UserApplication) o).id);
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
        return "UserApplication{" +
                "id=" + getId() +
                ", phone='" + getPhone() + "'" +
                ", passport='" + getPassport() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", isGoogleAccount='" + getIsGoogleAccount() + "'" +
                ", isFacebookAccount='" + getIsFacebookAccount() + "'" +
                ", isTwitterAccount='" + getIsTwitterAccount() + "'" +
                ", image='" + getImage() + "'" +
                ", rate=" + getRate() +
                ", fiveStar=" + getFiveStar() +
                ", fourSatr=" + getFourSatr() +
                ", threeStar=" + getThreeStar() +
                ", twoStar=" + getTwoStar() +
                ", oneStar=" + getOneStar() +
                ", detalis='" + getDetalis() + "'" +
                "}";
    }
}
