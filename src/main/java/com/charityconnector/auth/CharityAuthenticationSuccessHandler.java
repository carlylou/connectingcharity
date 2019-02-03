package com.charityconnector.auth;

import com.charityconnector.entity.Charity;
import com.charityconnector.service.CharityService;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CharityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CharityAuthenticationSuccessHandler.class);

    private final CharityService charityService;

    public CharityAuthenticationSuccessHandler(CharityService charityService) {
        this.charityService = charityService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2Authentication oauthAuthentication = (OAuth2Authentication) authentication;
        MyOAuth2AuthenticationDetails userDetails = new MyOAuth2AuthenticationDetails(request);
        String userId = oauthAuthentication.getUserAuthentication().getPrincipal().toString();

        userDetails.setCharity(true);

        //check if the user has a charity already
        Charity connectedCharity = charityService.findByOauthUserId(userId);

        if (connectedCharity == null) {
            //if not

            //create a new charity with that userId
            connectedCharity = new Charity(userId);
            connectedCharity.setDescription("Your charity description");
            connectedCharity.setName("Your charity name");

            charityService.addCharity(connectedCharity);

            //get it again to have the DB id
            connectedCharity = charityService.findByOauthUserId(userId);
            Long connectedCharityId = connectedCharity.getId();

            userDetails.setCharityId(connectedCharityId);

            //redirect to create new charity
            response.sendRedirect("/charityPage/" + connectedCharityId);
        } else {
            //if exist redirect to charity page
            Long connectedCharityId = connectedCharity.getId();

            userDetails.setCharityId(connectedCharityId);

            //redirect to create new charity
            response.sendRedirect("/charityPage/" + connectedCharityId);
        }

        oauthAuthentication.setDetails(userDetails);

    }
}