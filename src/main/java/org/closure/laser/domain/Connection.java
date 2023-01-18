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
 * The Employee entity.
 */
@Schema(description = "The Employee entity.")
@Entity
@Table(name = "connection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Connection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "local_token")
    private String localToken;

    @Column(name = "local_refresh_token")
    private String localRefreshToken;

    @Column(name = "o_auth_token")
    private String oAuthToken;

    @Column(name = "local_token_expiry_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date localTokenExpiryDate;

    @JsonIgnoreProperties(value = { "connection", "tripsDeals", "shipmentDeals", "user" }, allowSetters = true)
    @OneToOne(mappedBy = "connection")
    private UserApplication userApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Connection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFcmToken() {
        return this.fcmToken;
    }

    public Connection fcmToken(String fcmToken) {
        this.setFcmToken(fcmToken);
        return this;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getLocalToken() {
        return this.localToken;
    }

    public Connection localToken(String localToken) {
        this.setLocalToken(localToken);
        return this;
    }

    public void setLocalToken(String localToken) {
        this.localToken = localToken;
    }

    public String getLocalRefreshToken() {
        return this.localRefreshToken;
    }

    public Connection localRefreshToken(String localRefreshToken) {
        this.setLocalRefreshToken(localRefreshToken);
        return this;
    }

    public void setLocalRefreshToken(String localRefreshToken) {
        this.localRefreshToken = localRefreshToken;
    }

    public String getoAuthToken() {
        return this.oAuthToken;
    }

    public Connection oAuthToken(String oAuthToken) {
        this.setoAuthToken(oAuthToken);
        return this;
    }

    public void setoAuthToken(String oAuthToken) {
        this.oAuthToken = oAuthToken;
    }

    public Date getLocalTokenExpiryDate() {
        return this.localTokenExpiryDate;
    }

    public Connection localTokenExpiryDate(Date localTokenExpiryDate) {
        this.setLocalTokenExpiryDate(localTokenExpiryDate);
        return this;
    }

    public void setLocalTokenExpiryDate(Date localTokenExpiryDate) {
        this.localTokenExpiryDate = localTokenExpiryDate;
    }

    public UserApplication getUserApplication() {
        return this.userApplication;
    }

    public void setUserApplication(UserApplication userApplication) {
        if (this.userApplication != null) {
            this.userApplication.setConnection(null);
        }
        if (userApplication != null) {
            userApplication.setConnection(this);
        }
        this.userApplication = userApplication;
    }

    public Connection userApplication(UserApplication userApplication) {
        this.setUserApplication(userApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Connection)) {
            return false;
        }
        return id != null && id.equals(((Connection) o).id);
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
        return "Connection{" +
                "id=" + getId() +
                ", fcmToken='" + getFcmToken() + "'" +
                ", localToken='" + getLocalToken() + "'" +
                ", localRefreshToken='" + getLocalRefreshToken() + "'" +
                ", oAuthToken='" + getoAuthToken() + "'" +
                ", localTokenExpiryDate='" + getLocalTokenExpiryDate() + "'" +
                "}";
    }
}
