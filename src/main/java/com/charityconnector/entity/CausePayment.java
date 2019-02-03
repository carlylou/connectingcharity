package com.charityconnector.entity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "cause_payment")
public class CausePayment {
    private Long id;
    private Long causeId;
    private Double amount;
    private Date date;
    private String transactionId;


    /* Required by JPA specification */
    public CausePayment() {
        super();
    }

    public CausePayment(Long id, Long causeId, Double amount, Date date, String transactionId) {
        this.id = id;
        this.causeId = causeId;
        this.amount = amount;
        this.date = new Date();
        this.transactionId = transactionId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "cause_id")
    public long getCauseId() {
        return causeId;
    }

    public void setCauseId(long causeId) {
        this.causeId = causeId;
    }

    @Column(name = "amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


}


