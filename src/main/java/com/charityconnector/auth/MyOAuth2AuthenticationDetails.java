package com.charityconnector.auth;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public class MyOAuth2AuthenticationDetails extends OAuth2AuthenticationDetails {

    public static MyOAuth2AuthenticationDetails getAuthenticationDetails(Principal principal) {
        MyOAuth2AuthenticationDetails authDetails = null;
        OAuth2Authentication auth;
        if (principal != null) {
            auth = (OAuth2Authentication) principal;
            authDetails = (MyOAuth2AuthenticationDetails) auth.getDetails();
            return authDetails;
        } else {
            return null;
        }
    }

    public static void putAuthenticationDetails(Principal principal, Map<String, Object> model) {
        MyOAuth2AuthenticationDetails authDetails = getAuthenticationDetails(principal);

        if (principal != null)
            model.put("userId", principal.getName());

        if (authDetails != null) {
            model.put("isCharity", Boolean.toString(authDetails.isCharity()));
            if (authDetails.isCharity())
                model.put("charityId", authDetails.getCharityId());
        } else {
            model.put("isCharity", "false");
        }
    }

    private boolean isCharity;
    private Long charityId;

    /**
     * Records the access token value and remote address and will also set the session Id if a session already exists
     * (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public MyOAuth2AuthenticationDetails(HttpServletRequest request) {
        super(request);
    }

    public boolean isCharity() {
        return isCharity;
    }

    public void setCharity(boolean charity) {
        isCharity = charity;
    }

    public Long getCharityId() {
        return charityId;
    }

    public void setCharityId(Long charityId) {
        this.charityId = charityId;
    }
}
