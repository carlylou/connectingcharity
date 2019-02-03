package com.charityconnector.serviceImpl;

import com.charityconnector.dao.CountryRepository;
import com.charityconnector.entity.Country;
import com.charityconnector.service.CountryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Resource
    CountryRepository countryRepository;

    @Override
    public List<String> getAllCountries() {
        List<String> res = new ArrayList<>();
        List<Country> countries = countryRepository.findAll();
        for (Country country : countries) {
            res.add(country.getCountryValue());
        }
        return res;
    }

    @Override
    public Country findCountryByName(String name) {
        return countryRepository.findCountryByName(name);
    }
}
