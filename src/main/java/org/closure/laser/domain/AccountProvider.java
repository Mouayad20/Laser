package org.closure.laser.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountProvider.
 */
@Entity
@Table(name = "account_provider")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccountProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "provider", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deal", "provider" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountProvider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AccountProvider name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setProvider(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setProvider(this));
        }
        this.transactions = transactions;
    }

    public AccountProvider transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public AccountProvider addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setProvider(this);
        return this;
    }

    public AccountProvider removeTransactions(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setProvider(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountProvider)) {
            return false;
        }
        return id != null && id.equals(((AccountProvider) o).id);
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
        return "AccountProvider{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                "}";
    }
}
