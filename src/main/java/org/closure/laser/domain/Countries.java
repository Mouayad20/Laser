package org.closure.laser.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Countries.
 */
@Entity
@Table(name = "countries")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Countries implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "capital")
    private String capital;

    @Column(name = "code")
    private String code;

    @Column(name = "phone_code")
    private String phoneCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Countries() {}

    public Countries(String code, String capital, String phoneCode, String country) {
        this.country = country;
        this.capital = capital;
        this.code = code;
        this.phoneCode = phoneCode;
    }

    public Long getId() {
        return this.id;
    }

    public Countries id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public Countries country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCapital() {
        return this.capital;
    }

    public Countries capital(String capital) {
        this.setCapital(capital);
        return this;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCode() {
        return this.code;
    }

    public Countries code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneCode() {
        return this.phoneCode;
    }

    public Countries phoneCode(String phoneCode) {
        this.setPhoneCode(phoneCode);
        return this;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Countries)) {
            return false;
        }
        return id != null && id.equals(((Countries) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Countries{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", capital='" + getCapital() + "'" +
            ", code='" + getCode() + "'" +
            ", phoneCode='" + getPhoneCode() + "'" +
            "}";
    }
}
