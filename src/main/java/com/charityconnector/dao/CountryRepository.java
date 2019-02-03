package com.charityconnector.dao;

import com.charityconnector.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findCountryByCountryValue(String countryValue);

    @Query("select c from Country c where c.countryValue LIKE  CONCAT('%',:name,'%')")
    Country findCountryByName(@Param("name") String name);

}
