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
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals(PROVIDER_GO.getAttribute())) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals(PROVIDER_NA.getAttribute())) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        UserInformation userInformation = UserInformation.builder()
                .role("ROLE_USER")
                .email(oAuth2Response.getEmail())
                .name(oAuth2Response.getName())
                .username(oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId())
                .provider(oAuth2Response.getProvider())
                .build();

        updateOrSaveUser(userInformation);

        return new CustomOAuth2User(userInformation);
    }

    private void updateOrSaveUser(UserInformation userInformation) {
        userRepository.findByUsername(userInformation.getUsername())
                .ifPresentOrElse(
                        u -> u.update(userInformation),
                        () -> userRepository.save(User.create(userInformation)));
    }

}
