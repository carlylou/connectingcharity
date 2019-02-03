package com.charityconnector.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "activity")
public class Activity {
    private Long id;
    private String country;
    private Date holdDate;
    private Set<Donor> donors;
    private Charity charity;
    private String content;
    private Date insertTime;
    private Date updateTime;
    @Transient
    private int numDonors;
    private String title;

    public Activity() {
        super();
    }

    public Activity(Long id, String country, Date holdDate, String title) {
        this.id = id;
        this.country = country;
        this.holdDate = holdDate;
        this.title = title;
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

    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "hold_date")
    public Date getHoldDate() {
        return holdDate;
    }

    public void setHoldDate(Date holdDate) {
        this.holdDate = holdDate;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "activities_donar", joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "donor_id", referencedColumnName = "id"))
    @JsonIgnore
    public Set<Donor> getDonors() {
        return donors;
    }
    public void setDonors(Set<Donor> donors) {
        this.donors = donors;
    }

    public Set<Donor> addVolunteerDonor(Donor donor) {
        donors.add(donor);
        Set<Activity> activities = donor.getActivities();
        activities.add(this);
        return donors;
    }

    @ManyToOne
    @JoinColumn(name = "charity_id")
    public Charity getCharity() {
        return charity;
    }
    public void setCharity(Charity charity) {
        this.charity = charity;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Column(name="insert_time")
    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    @Column(name="update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Transient
    public int getNumDonors() {
        numDonors = donors.size();
        return numDonors;
    }

    public void setNumDonors(int numDonors) {
        this.numDonors = numDonors;
    }
}
