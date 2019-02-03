package com.charityconnector.controller;

import com.charityconnector.service.CountryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class CountryController {

    @Resource
    CountryService countryService;

    @RequestMapping(path = "/countries", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getCharityPage() {
        return countryService.getAllCountries();
    }
}
