package com.charityconnector.entity;

import javax.persistence.*;

@Entity
@Table(name = "uk_record_charity")
public class UKRecordCharity {
    private String email;

    public UKRecordCharity(String email){
        this.email = email;
    }

    public UKRecordCharity() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
