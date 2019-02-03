package com.charityconnector.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "country")
public class Country {
    private Long id;
    private String countryValue;
    private Set<Charity> charities;

    /* Required by JPA specification */
    public Country() {
        super();
    }

    public Country(Long id, String countryValue, Set<Charity> charities) {
        this.id = id;
        this.countryValue = countryValue;
        this.charities = charities;
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

    @Column(name = "country_value")
    public String getCountryValue() {
        return countryValue;
    }


    public void setCountryValue(String countryValue) {
        this.countryValue = countryValue;
    }

    @ManyToMany(mappedBy = "countries")
    @JsonIgnore
    public Set<Charity> getCharities() {
        return charities;
    }

    public void setCharities(Set<Charity> charities) {
        this.charities = charities;
    }

}



