package com.charityconnector.controller;


import com.charityconnector.auth.MyOAuth2AuthenticationDetails;
import com.charityconnector.entity.Charity;
import com.charityconnector.entity.UKRecordCharity;
import com.charityconnector.service.ArticleService;
import com.charityconnector.service.CharityService;
import com.charityconnector.service.UKRecordCharityService;
import com.charityconnector.util.CodeUtil;
import com.charityconnector.util.MailUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

import static com.charityconnector.auth.MyOAuth2AuthenticationDetails.getAuthenticationDetails;
import static com.charityconnector.auth.MyOAuth2AuthenticationDetails.putAuthenticationDetails;


@Controller
public class CharityController {

    @Resource
    private CharityService charityService;

    @Resource
    private ArticleService articleService;

    @Resource
    private UKRecordCharityService ukRecordCharityService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CharityController.class);

    @RequestMapping(path = "/charities/{name}", method = RequestMethod.GET)
    @ResponseBody
    Charity[] getCharitiesByName(@PathVariable("name") String name) {
        return charityService.findByName(name);
    }

    @RequestMapping(path = "/charity/{id}", method = RequestMethod.GET)
    @ResponseBody
    Charity getCharityById(@PathVariable("id") Long id) {
        return charityService.findById(id);
    }

    @RequestMapping(path = "/charity", method = RequestMethod.POST)
    @ResponseBody
    public Charity addCharity(@RequestBody Charity charity, Principal principal) {
        MyOAuth2AuthenticationDetails authDetails = getAuthenticationDetails(principal);
        if (authDetails == null || !authDetails.isCharity() || !Objects.equals(authDetails.getCharityId(), charity.getId()))
            return null;

        return charityService.addCharity(charity);
    }

    @RequestMapping(path = "/charity/thumbUp", method = RequestMethod.PATCH)
    @ResponseBody
    public Long thumbUp(@RequestBody Charity charity) {
        return charityService.thumbUp(charity.getId()).getThumbUp();
    }

    @RequestMapping(path = "/charity/thumbUpUnique", method = RequestMethod.PATCH)
    @ResponseBody
    public int thumbUpUnique(@RequestBody Charity charity, Principal principal) {
        if (principal == null) {
            return -1;
        }
        String oauthId = principal.getName();
        System.out.println(oauthId);
        if (oauthId == null) {
            return -1;
        }
        MyOAuth2AuthenticationDetails details = MyOAuth2AuthenticationDetails.getAuthenticationDetails(principal);
        if (details.isCharity()) {
            return -3; // need to login as donor to thumb up
        }
        return charityService.thumbUpUnique(charity.getId(), oauthId);
    }

    @RequestMapping(path = "/charity/random", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getRandomCharity() {
        Charity res = null;
        while (res == null)
            res = charityService.findRandom();
        return new ModelAndView("redirect:/charityPage/" + res.getId());
    }


    @RequestMapping(path = "/charity/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteCharityById(@PathVariable("id") Long id, Principal principal) {
        MyOAuth2AuthenticationDetails authDetails = getAuthenticationDetails(principal);
        if (authDetails == null || !authDetails.isCharity() || !Objects.equals(authDetails.getCharityId(), id))
            return;

        charityService.deleteById(id);
    }

    @RequestMapping(path = "/charity", method = RequestMethod.PATCH)
    @ResponseBody
    public void updateCharity(@RequestBody Charity charity, Principal principal) {
        MyOAuth2AuthenticationDetails authDetails = getAuthenticationDetails(principal);
        if (authDetails == null || !authDetails.isCharity() || !Objects.equals(authDetails.getCharityId(), charity.getId())){
            return;}
        charityService.updateSelective(charity);
    }

    @RequestMapping("/charityPage/{id}")
    public String getCharityPage(Map<String, Object> model, @PathVariable("id") String charityId, Principal principal) {
        Long id = Long.valueOf(charityId.replaceAll(",", "").toString());

        putAuthenticationDetails(principal, model);

        model.put("charity", charityService.findById(id));
        model.put("articles", articleService.findArticlesByCharityId(id));
        model.put("activities", charityService.findById(id).getActivities());

        return "charityPage";
    }

    // This method is used for upload the logo of the charity.
    // The image is encoded with base64 and then store directly to the databse.
    @RequestMapping(path = "/charity/{id}/logo", method = RequestMethod.POST)
    public String updateCharityLogo(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id, Principal principal) {
        MyOAuth2AuthenticationDetails authDetails = getAuthenticationDetails(principal);
        if (authDetails == null || !authDetails.isCharity() || !Objects.equals(authDetails.getCharityId(), id))
            return "error";


        Charity charity = charityService.findById(id);
        if (!file.isEmpty()) {
            try {
                charity.setLogoFile("data:" + file.getContentType() + ";base64," + new BASE64Encoder().encode(file.getBytes()).replaceAll("\r|\n", ""));
            } catch (IOException e) {
                e.printStackTrace();
                return "File Error";
            }
            charityService.updateDirect(charity);
            return ("redirect:/charityPage/" + id);
        } else {
            return "Update File, the File is empty.";
        }
    }



    @RequestMapping(path = "/charity/{id}/verify", method = RequestMethod.POST)
    public ResponseEntity<String> verifyCharity(@PathVariable("id") Long id, Principal principal) {
        MyOAuth2AuthenticationDetails authDetails = getAuthenticationDetails(principal);
        if (authDetails == null || !authDetails.isCharity() || !Objects.equals(authDetails.getCharityId(), id))
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);

        Charity charity = charityService.findById(id);
        String email = charity.getEmail();
        if (email.equals("")) {
            return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);
        } else {
            UKRecordCharity result = ukRecordCharityService.findByEmail(email);
            if (result == null) {
                return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
            } else {
                //Generate the verify code and save it to the database;
                String code = CodeUtil.generateUniqueCode();
                Long charityID = charity.getId();
                charity.setVerifyCode(code);
                charityService.updateDirect(charity);
                new Thread(new MailUtil(email, code, charityID)).start();
                return new ResponseEntity<String>(HttpStatus.OK);
            }
        }
    }

    @RequestMapping(path = "/charities/cause/{cause}", method = RequestMethod.GET)
    @ResponseBody
    public Object[] getCharityByCause(@PathVariable("cause") String cause) {
        return charityService.getCharitiesByCause(cause).toArray();
    }

    @RequestMapping(path = "/charities/country/{country}", method = RequestMethod.GET)
    @ResponseBody
    public Object[] getCharityByCountry(@PathVariable("country") String country) {
        return charityService.getCharitiesByCountry(country).toArray();
    }

    /**
     * Spring will automatically resemble the pageable parameter based on the request parameter value.
     * Those parameters are as follows:
     * page: represent the current page index from 0
     * size: the size of one page
     * sort: the info of how to sort. For example: if I write request param like: sort=thumbUp,desc , then
     * we get charities sorted by thumbUp in DESC order.
     * The attribute value for sort is the name of attribute in Charity Entity not the name in database table.
     *
     * @param pageable default value is sort by charity id in DESC order and the default page size is 5.
     * @return
     */
    @RequestMapping(path = "/charities/rank", method = RequestMethod.GET)
    @ResponseBody
    public Page<Charity> getEntryByPageable(@PageableDefault(value = 5, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return charityService.findAll(pageable);
    }

    @RequestMapping("/charityCreationPage/{id}")
    public String getCharityCreationPage(Map<String, Object> model, @PathVariable("id") Long id, Principal principal) {
        MyOAuth2AuthenticationDetails authDetails = getAuthenticationDetails(principal);
        if (authDetails == null || !authDetails.isCharity() || !Objects.equals(authDetails.getCharityId(), id))
            return "error";

        model.put("charity", charityService.findById(id));

        return "charityCreationPage";
    }
}
