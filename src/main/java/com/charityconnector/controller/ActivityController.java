package com.charityconnector.controller;

import com.charityconnector.auth.MyOAuth2AuthenticationDetails;
import com.charityconnector.entity.Activity;
import com.charityconnector.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

@Controller
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @RequestMapping(path = "/activities/{charityId}", method = RequestMethod.GET)
    @ResponseBody
    Activity[] getActivitiesByCharityId(@PathVariable("charityId") Long charityId) {
        return activityService.findArticlesByCharityId(charityId);
    }

    @RequestMapping(path = "/activity/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Activity getActivityById(@PathVariable("id") Long id) {
        return activityService.findById(id);
    }

    @RequestMapping(path = "/activity/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteActivityById(@PathVariable("id") Long id) {
        activityService.deleteById(id);
    }

    @RequestMapping(path = "/activity/{charityId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addActivity(@RequestBody Activity activity, @PathVariable("charityId") Long charityId) {
        activityService.addActivity(activity, charityId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(path = "/activity", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<String> updateActivity(@RequestBody Activity activity) {
        activityService.updateSelective(activity);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(path = "/activity/volunteer", method = RequestMethod.PATCH)
    @ResponseBody
    public int volunteer(@RequestBody Activity activity, Principal principal) {
        if (principal == null) {
            return -1;
        }
        String id = principal.getName();
        System.out.println(id);
        if (id == null) {
            return -1;
        }
        MyOAuth2AuthenticationDetails details = MyOAuth2AuthenticationDetails.getAuthenticationDetails(principal);
        if (details.isCharity()) {
            return -3; // need to login as donor to volunteer
        }
        return activityService.volunteer(activity.getId(), id);
    }


}
