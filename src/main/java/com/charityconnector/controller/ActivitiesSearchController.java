package com.charityconnector.controller;

import com.charityconnector.entity.Activity;
import com.charityconnector.entity.Country;
import com.charityconnector.service.ActivityService;
import com.charityconnector.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;

import static com.charityconnector.auth.MyOAuth2AuthenticationDetails.putAuthenticationDetails;


@Controller
public class ActivitiesSearchController {

    @Resource
    private ActivityService activityService;

    @Resource
    private CountryService countryService;

    @Autowired
    public ActivitiesSearchController(CountryService countryService, ActivityService activityService) {
        this.activityService = activityService;
        this.countryService = countryService;
    }

    @RequestMapping("/results/activities")
    public String getResultsPage(Map<String, Object> model,
                                 @RequestParam(required = false) String holdDateFrom,
                                 @RequestParam(required = false) String holdDateTo,
                                 @RequestParam(required = false) String country,
                                 @RequestParam(defaultValue = "0") int pageNumber,
                                 @RequestParam(defaultValue = "10") int pageSize,
                                 Principal principal) {
        Page<Activity> page;
        Pageable pageRequest;
        Date from = null, to = null;
        Country selectedCountry;

        putAuthenticationDetails(principal, model);

        model.put("countries", countryService.getAllCountries());

        pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.DESC, "holdDate");

        try {
            if (holdDateFrom != null && !holdDateFrom.equals(""))
                from = Date.from(Instant.from(Instant.parse(holdDateFrom + "T00:00:00.00Z").atOffset(ZoneOffset.UTC).minus(Duration.ofSeconds(1))));
            if (holdDateTo != null && !holdDateTo.equals(""))
                to = Date.from(Instant.from(Instant.parse(holdDateTo + "T00:00:00.00Z").atOffset(ZoneOffset.UTC).plus(Duration.ofDays(1))));
        } catch (DateTimeParseException e) {
            from = null;
            to = null;
        }
        if (country == null || country.equals("Country"))
            selectedCountry = null;
        else
            selectedCountry = countryService.findCountryByName(country);

        page = activityService.findByHoldDateAndCountry(from, to, selectedCountry, pageRequest);

        if (page.getContent().size() != 0) {
            model.put("activities", page.getContent());
            model.put("numberOfPages", page.getTotalPages());
            model.put("numberOfResults", page.getTotalElements());
            model.put("pageNumber", page.getNumber());
            model.put("pageSize", pageSize);
            model.put("thisPageSize", page.getContent().size());
        }

        return "activitiesResultsPage";
    }
}
