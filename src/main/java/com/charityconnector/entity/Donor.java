package com.charityconnector.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "donor")
public class Donor {
    private Long id;
    private Set<Paypal> payments;
    private Set<Charity> thumbUpCharities;
    private Set<Activity> activities;
    private String oauthId;


    /* Required by JPA specification */
    public Donor() {
        super();
    }

    public Donor(String oauthId) {
        this.oauthId = oauthId;
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

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    public Set<Paypal> getPayments() {
        return payments;
    }

    public void setPayments(Set<Paypal> payments) {
        this.payments = payments;
    }

    @ManyToMany(mappedBy = "thumbUpDonors")
    @JsonIgnore
    public Set<Charity> getThumbUpCharities() {
        return thumbUpCharities;
    }

    public void setThumbUpCharities(Set<Charity> thumbUpCharities) {
        this.thumbUpCharities = thumbUpCharities;
    }

    @ManyToMany(mappedBy = "donors")
    @JsonIgnore
    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    @Column(name = "oauth_id")
    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }
}
