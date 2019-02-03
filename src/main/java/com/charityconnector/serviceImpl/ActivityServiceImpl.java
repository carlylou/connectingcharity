package com.charityconnector.serviceImpl;

import com.charityconnector.dao.ActivityRepository;
import com.charityconnector.dao.CharityRepository;
import com.charityconnector.dao.DonorRepository;
import com.charityconnector.entity.Activity;
import com.charityconnector.entity.Charity;
import com.charityconnector.entity.Country;
import com.charityconnector.entity.Donor;
import com.charityconnector.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityRepository activityRepository;

    @Resource
    private CharityRepository charityRepository;

    @Resource
    private DonorRepository donorRepository;

    @Override
    public Activity findById(Long id) {
        return activityRepository.findOne(id);
    }

    @Override
    public void deleteById(Long id) {
        Activity activity = activityRepository.findOne(id);
        Charity charity = activity.getCharity();
        charity.deleteOneActivity(activity);
        charityRepository.save(charity);
        activityRepository.delete(activity);
    }

    @Override
    public void addActivity(Activity activity, Long charityId) {
        Date now = new Date();
        activity.setInsertTime(now);
        activity.setUpdateTime(now);
        Charity charity = charityRepository.findOne(charityId);
        charity.addActivities(activity);
        charityRepository.save(charity);
    }

    @Override
    public void updateSelective(Activity activity) {
        Activity readyToUpdate;
        if (activity.getId() == null) {
            return;
        } else {
            Activity origin = activityRepository.findOne(activity.getId());
            readyToUpdate = origin;
            readyToUpdate.setId(activity.getId());

            String title = activity.getTitle() == null ? origin.getTitle() : activity.getTitle();
            readyToUpdate.setTitle(title);

            String content = activity.getContent() == null ? origin.getContent() : activity.getContent();
            readyToUpdate.setContent(content);

            String country = activity.getCountry() == null ? origin.getCountry() : activity.getCountry();
            readyToUpdate.setCountry(country);

            Date holdDate = activity.getHoldDate() == null ? origin.getHoldDate() : activity.getHoldDate();
            readyToUpdate.setHoldDate(holdDate);
        }
        Date now = new Date();
        readyToUpdate.setUpdateTime(now);
        activityRepository.save(readyToUpdate);
    }

    @Override
    public void updateDirect(Activity activity) {
        Date now = new Date();
        activity.setUpdateTime(now);
        activityRepository.save(activity);
    }

    @Override
    public Activity[] findArticlesByCharityId(Long charityId) {
        Set<Activity> temp = charityRepository.findOne(charityId).getActivities();
        return temp.toArray(new Activity[temp.size()]);
    }

    @Override
    public int volunteer(Long activityId, String donorOauthId) {
        Activity activity = activityRepository.findOne(activityId);
        Set<Donor> donors = activity.getDonors();
        int res = 0;
        for (Donor donor : donors) {
            if (donor.getOauthId().equals(donorOauthId)) {
                res = -2; // represent this donor has volunteered
                break;
            }
        }
        if (res != 0) {
            return res;
        }
        Donor donor = donorRepository.findByOauthId(donorOauthId);
        activity.addVolunteerDonor(donor);
        activityRepository.save(activity);
        return activity.getNumDonors();
    }

    @Override
    public Page<Activity> findByHoldDateAndCountry(Date holdDateFrom, Date holdDateTo, Country country, Pageable pageable) {
        if (holdDateFrom == null)
            holdDateFrom = new Date(0L);
        if (holdDateTo == null)
            holdDateTo = new Date(9999999999999L);
        if (country == null)
            return activityRepository.findByHoldDateBetween(holdDateFrom, holdDateTo, pageable);

        return activityRepository.findByCountryAndHoldDateBetween(country.getCountryValue(), holdDateFrom, holdDateTo, pageable);
    }
}
