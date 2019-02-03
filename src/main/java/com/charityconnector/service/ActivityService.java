package com.charityconnector.service;

import com.charityconnector.entity.Activity;
import com.charityconnector.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ActivityService {

    Activity findById(Long id);

    void deleteById(Long id);

    void addActivity(Activity activity, Long charityId);

    void updateSelective(Activity activity);

    void updateDirect(Activity activity);

    Activity[] findArticlesByCharityId(Long charityId);

    int volunteer(Long id, String donorOauthId);

    Page<Activity> findByHoldDateAndCountry(Date holdDateFrom, Date holdDateTo, Country country, Pageable pageable);
}
