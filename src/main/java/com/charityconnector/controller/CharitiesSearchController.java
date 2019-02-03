package com.charityconnector.controller;

import com.charityconnector.entity.Cause;
import com.charityconnector.entity.Charity;
import com.charityconnector.entity.Country;
import com.charityconnector.service.CauseService;
import com.charityconnector.service.CharityService;
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
import java.util.List;
import java.util.Map;

import static com.charityconnector.auth.MyOAuth2AuthenticationDetails.putAuthenticationDetails;


@Controller
public class CharitiesSearchController {

    @Resource
    private CharityService charityService;

    @Resource
    private CountryService countryService;

    @Resource
    private CauseService causeService;



    @Autowired
    public CharitiesSearchController(CharityService charityService) {
        this.charityService = charityService;
    }

    @RequestMapping("/results/charities")
    public String getResultsPage(Map<String, Object> model,
                                 @RequestParam(name = "searchString", defaultValue = "") String searchString,
                                 @RequestParam(name = "causeString", defaultValue = "Causes") String causeString,
                                 @RequestParam(name = "countryString", defaultValue = "Countries") String countryString,
                                 @RequestParam(defaultValue = "0") int pageNumber,
                                 @RequestParam(defaultValue = "10") int pageSize,
                                 Principal principal) {

        model.put("causeOptions",causeService.getAllCausesName());
        model.put("countryOptions",countryService.getAllCountries());
        model.put("currentCause",causeString);
        model.put("currentCountry",countryString);

        putAuthenticationDetails(principal, model);

        Pageable pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.DESC, "name");
        Page<Charity> page = null;

        if(causeString.equals("Causes") && countryString.equals("Countries") && searchString.trim().equals("")){
            page = charityService.findAll(pageRequest);
        }else if(causeString.equals("Causes") && countryString.equals("Countries")){
            page = charityService.findByNameOrDescriptionLike(searchString,pageRequest);
        }else if(!causeString.equals("Causes") && !countryString.equals("Countries")){
            Cause cause = causeService.findByName(causeString);
            Country country = countryService.findCountryByName(countryString);
            page = charityService.findByCauseAndCountry(cause,country,searchString,pageRequest);
        }else if(!causeString.equals("Causes")){
            Cause cause = causeService.findByName(causeString);
            page = charityService.findByCause(cause,searchString,pageRequest);
        }else{
            Country country = countryService.findCountryByName(countryString);
            page = charityService.findByCountry(country,searchString,pageRequest);
        }


        if (page == null) {
            model.put("searchedName", searchString);
            return "resultsPage";
        }
        List<Charity> charities = page.getContent();
        if (charities.size() != 0) {
            model.put("charities", charities);
            model.put("numberOfPages", page.getTotalPages());
            model.put("numberOfResults", page.getTotalElements());
            model.put("pageNumber", pageNumber);
            model.put("searchedName", searchString);
            model.put("pageSize", pageSize);
            model.put("thisPageSize", charities.size());
        }
        return "charitiesResultsPage";
    }

}
