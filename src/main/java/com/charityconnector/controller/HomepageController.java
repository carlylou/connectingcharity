package com.charityconnector.controller;

import com.charityconnector.entity.Charity;
import com.charityconnector.service.CauseService;
import com.charityconnector.service.CharityService;
import com.charityconnector.service.CountryService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.charityconnector.auth.MyOAuth2AuthenticationDetails.putAuthenticationDetails;

@Controller
public class HomepageController {
    private static int FEATURED_PAGE_SIZE = 6;

    @Resource
    private CharityService charityService;

    @Resource
    private CountryService countryService;

    @Resource
    private CauseService causeService;

    @RequestMapping("/")
    public String getHomePage(Map<String, Object> model, Principal principal) {
        putAuthenticationDetails(principal, model);

        List<Charity> featuredCharities = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.DESC, "thumbUp");
        List<Charity> res = charityService.findAll(sort);
        while (featuredCharities.size() < FEATURED_PAGE_SIZE) {
            if(res.size() >= featuredCharities.size()){
                Charity c = res.get(featuredCharities.size());
                featuredCharities.add(c);
            }
        }
        model.put("causeOptions",causeService.getAllCausesName());
        model.put("countryOptions",countryService.getAllCountries());
        model.put("featuredCharities", featuredCharities);
        return "index";
    }
}
