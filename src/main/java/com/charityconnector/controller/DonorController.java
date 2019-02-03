package com.charityconnector.controller;


import com.charityconnector.entity.Donor;
import com.charityconnector.entity.Paypal;
import com.charityconnector.service.CharityService;
import com.charityconnector.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.charityconnector.auth.MyOAuth2AuthenticationDetails.putAuthenticationDetails;

@Controller
public class DonorController {
    @Autowired
    DonorService donorService;

    @Autowired
    CharityService charityService;

    @RequestMapping("/donorPage/{oauthId}")
    public String getDonorPage(Map<String, Object> model, @PathVariable("oauthId") String oauthId, Principal principal) {
        if (principal == null || !principal.getName().equals((oauthId)))
            return "error";

        Donor donor = donorService.findByOauthId(principal.getName());
        Map<String, Object>[] payments = new Map[donor.getPayments().size()];
        Iterator<Paypal> iterator = donor.getPayments().iterator();

        putAuthenticationDetails(principal, model);

        for (int i = 0; i < donor.getPayments().size(); i++) {
            payments[i] = new HashMap<String, Object>();
            Paypal paymentDetails = iterator.next();
            payments[i].put("paymentDetails", paymentDetails);
            payments[i].put("charity", charityService.findById(paymentDetails.getCharityId()));

        }
        model.put("payments", payments);
        model.put("donor", donor);

        return "donorPage";
    }
}
