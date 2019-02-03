package com.charityconnector.controller;

import com.charityconnector.entity.Cause;
import com.charityconnector.service.CauseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.charityconnector.auth.MyOAuth2AuthenticationDetails.putAuthenticationDetails;

@Controller
public class CauseController {

    @Resource
    private CauseService causeService;


    @RequestMapping("/causePage/{id}")
    public String getCharityPage(Map<String, Object> model, @PathVariable("id") Long id, Principal principal) {
        Cause cause = causeService.findById(id);
        putAuthenticationDetails(principal, model);
        model.put("cause",cause);
        model.put("charities",cause.getCharities());
        return "causePage";
    }

    @RequestMapping("/causesPage")
    public String getAllCauses(Map<String, Object> model, Principal principal) {
        putAuthenticationDetails(principal, model);
        List<Cause> causes = causeService.getAllCauses();
        model.put("causes",causes);
        return "causesPage";
    }

    @RequestMapping(path = "/causes", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAllCausesName() {
        return causeService.getAllCausesName();
    }

}
