package com.charityconnector.controller;


import com.charityconnector.entity.Charity;
import com.charityconnector.service.CharityService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class VerifyController {

    @Resource
    private CharityService charityService;

    static final int CHARITY_VERIFIED = 1;

    @RequestMapping(path = "/verifyCharity/{id}/{code}", method = RequestMethod.GET)
    public String verifyCharity(@PathVariable("id") Long id,@PathVariable("code") String code) {
        Charity charity = charityService.findById(id);
        if(charity.getVerifyStatus()==CHARITY_VERIFIED)
            return "<h1>This charity has been verified before, you do not need to do it again!</h1>";
        if(charity.getVerifyCode().equals(code)){
            charity.setVerifyStatus(CHARITY_VERIFIED);
            charityService.updateDirect(charity);
            return "<h1>Verify Sucess!</h1>";
        }else{
            return "<h1>Error, the verify code does not match!</h1>";
        }
    }
}
