package org.closure.laser.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "to_account")
    private String toAccount;

    @Column(name = "fees")
    private Double fees;

    @Column(name = "net_amount")
    private Double netAmount;

    @Column(name = "details")
    private String details;

    @JsonIgnoreProperties(value = { "transaction", "shipments", "deliver", "owner", "trip", "status" }, allowSetters = true)
    @OneToOne(mappedBy = "transaction", fetch = FetchType.EAGER)
    private Deal deal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "transactions" }, allowSetters = true)
    private AccountProvider provider;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAccount() {
        return this.fromAccount;
    }

    public Transaction fromAccount(String fromAccount) {
        this.setFromAccount(fromAccount);
        return this;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return this.toAccount;
    }

    public Transaction toAccount(String toAccount) {
        this.setToAccount(toAccount);
        return this;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Double getFees() {
        return this.fees;
    }

    public Transaction fees(Double fees) {
        this.setFees(fees);
        return this;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public Double getNetAmount() {
        return this.netAmount;
    }

    public Transaction netAmount(Double netAmount) {
        this.setNetAmount(netAmount);
        return this;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public String getDetails() {
        return this.details;
    }

    public Transaction details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Deal getDeal() {
        return this.deal;
    }

    public void setDeal(Deal deal) {
        if (this.deal != null) {
            this.deal.setTransaction(null);
        }
        if (deal != null) {
            deal.setTransaction(this);
        }
        this.deal = deal;
    }

    public Transaction deal(Deal deal) {
        this.setDeal(deal);
        return this;
    }

    public AccountProvider getProvider() {
        return this.provider;
    }

    public void setProvider(AccountProvider accountProvider) {
        this.provider = accountProvider;
    }

    public Transaction provider(AccountProvider accountProvider) {
        this.setProvider(accountProvider);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", fromAccount='" + getFromAccount() + "'" +
            ", toAccount='" + getToAccount() + "'" +
            ", fees=" + getFees() +
            ", netAmount=" + getNetAmount() +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
