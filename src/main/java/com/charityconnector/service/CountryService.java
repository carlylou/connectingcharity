package com.charityconnector.service;

import com.charityconnector.entity.Country;

import java.util.List;

public interface CountryService {

    List<String> getAllCountries();

    Country findCountryByName(String name);
}
