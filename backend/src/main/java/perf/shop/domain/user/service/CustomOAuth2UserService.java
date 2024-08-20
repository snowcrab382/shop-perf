package perf.shop.domain.user.service;

import static perf.shop.domain.auth.domain.OAuth2Attributes.PROVIDER_GO;
import static perf.shop.domain.auth.domain.OAuth2Attributes.PROVIDER_NA;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.auth.dto.GoogleResponse;
import perf.shop.domain.auth.dto.NaverResponse;
import perf.shop.domain.auth.dto.OAuth2Response;
import perf.shop.domain.user.UserRepository;
import perf.shop.domain.user.domain.User;
import perf.shop.domain.user.dto.CustomOAuth2User;
import perf.shop.domain.user.dto.UserInformation;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("OAuth2User: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;
        if (registrationId.equals(PROVIDER_GO.getAttribute())) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals(PROVIDER_NA.getAttribute())) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        UserInformation userInformation = UserInformation.from(oAuth2Response);
        User user = userRepository.findByUsername(userInformation.getUsername())
                .orElseGet(() -> userRepository.save(User.from(userInformation)));
        userInformation.setUserId(user.getId());

        return new CustomOAuth2User(userInformation);
    }

}
