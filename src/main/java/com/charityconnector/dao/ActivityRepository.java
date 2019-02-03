package com.charityconnector.dao;

import com.charityconnector.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByCountryAndHoldDateBetween(String country, Date holdDate, Date holdDate2, Pageable pageable);

    Page<Activity> findByHoldDateBetween(Date holdDateFrom, Date holdDateTo, Pageable pageable);
}
