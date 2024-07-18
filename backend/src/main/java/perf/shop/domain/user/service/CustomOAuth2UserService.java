package perf.shop.domain.auth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import perf.shop.domain.auth.dto.CustomOAuth2User;
import perf.shop.domain.auth.dto.GoogleResponse;
import perf.shop.domain.auth.dto.NaverResponse;
import perf.shop.domain.auth.dto.OAuth2Response;
import perf.shop.domain.auth.dto.UserInformation;
import perf.shop.domain.auth.model.OAuth2Provider;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals(OAuth2Provider.GOOGLE.getProvider())) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals(OAuth2Provider.NAVER.getProvider())) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        return new CustomOAuth2User(UserInformation.from(oAuth2Response));
    }

}
