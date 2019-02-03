package com.charityconnector.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "charity")
public class Charity {


    private Long id;

    private Set<Cause> causes;
    private Set<Country> countries;
    private Set<Donor> thumbUpDonors;


    private String name;
    private String description;
    private String logoFile;
    private String email;
    private String paypalAccount;
    private String verifyCode;
    private int verifyStatus;
    private Long thumbUp;

    private String oauthUserId;
    private Set<Activity> activities;

    public Charity(Long id, String name, String description, String logoFile, String email, String paypalAccount,
                   Set<Cause> causes, Set<Country> countries, Set<Donor> thumbUpDonors,String verifyCode,
                   int verifyStatus, Long thumbUp,Set<Activity> activities ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logoFile = logoFile;
        this.email = email;
        this.paypalAccount = paypalAccount;
        this.causes = causes;
        this.countries = countries;
        this.thumbUpDonors = thumbUpDonors;
        this.verifyCode = verifyCode;
        this.verifyStatus = verifyStatus;
        this.thumbUp = thumbUp;
        this.activities = activities;
    }

    /* Required by JPA specification */
    public Charity() {
        super();
        this.thumbUp = 0L;
    }

    public Charity(String desciption, Long id) {
        this.id = id;
    }

    public Charity(String oauthUserId) {
        this();
        this.oauthUserId = oauthUserId;
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

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "logo_file")
    public String getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(String logoFile) {
        this.logoFile = logoFile;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "paypal_account")
    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }


    @Column(name = "verify_code")
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Column(name = "verify_status")
    public int getVerifyStatus() {
        return verifyStatus;
    }
    public void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "causes", joinColumns = @JoinColumn(name = "charity_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "cause_id", referencedColumnName = "id"))
    @JsonIgnore
    public Set<Cause> getCauses() {
        return causes;
    }
    public void setCauses(Set<Cause> causes) {
        this.causes = causes;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "countries", joinColumns = @JoinColumn(name = "charity_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "country_id", referencedColumnName = "id"))
    @JsonIgnore
    public Set<Country> getCountries() {
        return countries;
    }
    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }




    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "thumb_ups", joinColumns = @JoinColumn(name = "charity_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "donor_id", referencedColumnName = "id"))
    @JsonIgnore
    public Set<Donor> getThumbUpDonors() {
        return thumbUpDonors;
    }

    public void setThumbUpDonors(Set<Donor> thumbUpDonors) {
        this.thumbUpDonors = thumbUpDonors;
    }

    public Set<Donor> addThumbUpDonor(Donor donor) {
        thumbUpDonors.add(donor);
        Set<Charity> charities = donor.getThumbUpCharities();
        charities.add(this);
        return thumbUpDonors;
    }

    public Long getThumbUp() {
        if (thumbUpDonors == null)
            return 0L;
        return (long) thumbUpDonors.size();
    }

    public void setThumbUp(Long thumbUp) {
        this.thumbUp = thumbUp;
    }

    @Column(name = "oauth_user_id")
    public String getOauthUserId() {
        return oauthUserId;
    }

    public void setOauthUserId(String oauthUserId) {
        this.oauthUserId = oauthUserId;
    }


    @OneToMany(mappedBy = "charity", cascade = CascadeType.ALL)
    public Set<Activity> getActivities() {
        return activities;
    }
    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<Activity> addActivities(Activity activity) {
        activities.add(activity);
        activity.setCharity(this);
        return activities;
    }

    public Set<Activity> deleteOneActivity(Activity activity) {
        activities.remove(activity);
        return activities;
    }
}


