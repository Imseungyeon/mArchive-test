package syim.reviewboard.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import syim.reviewboard.config.auth.PrincipalDetail;
import syim.reviewboard.model.LoginType;
import syim.reviewboard.model.User;
import syim.reviewboard.model.UserRole;
import syim.reviewboard.repository.UserRepository;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("Entering loadUser method");
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("OAuth2User: " + oauth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("registrationId: " + registrationId);

        Map<String, Object> attributes = oauth2User.getAttributes();
        System.out.println("attributes: " + attributes);

        String providerId = null;
        String username = null;
        String email = null;

        if ("naver".equals(registrationId)) {
            System.out.println("Processing Naver login");
            attributes = (Map<String, Object>) attributes.get("response");
            providerId = attributes.get("id").toString();
            username = attributes.get("name") != null ? attributes.get("name").toString() : attributes.get("nickname").toString();
            email = attributes.get("email").toString();
            System.out.println("Naver user: " + username + ", email: " + email);
        } else if ("kakao".equals(registrationId)) {
            System.out.println("Processing Kakao login");
            providerId = attributes.get("id").toString();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            username = profile.get("nickname").toString();
            email = kakaoAccount.get("email").toString();
            System.out.println("Kakao user: " + username + ", email: " + email);
        }

//        String providerId = attributes.get("id").toString();
//        String username = attributes.get("name") != null ? attributes.get("name").toString() : attributes.get("nickname").toString();
//        String email = attributes.get("email").toString();

        User user = userRepository.findByProviderAndProviderId(registrationId, providerId);
        if (user == null) {
            user = new User();
            user.setProvider(registrationId);
            user.setProviderId(providerId);
            user.setUsername(username);
            user.setEmail(email);
            user.setLoginType(LoginType.valueOf(registrationId.toUpperCase()));
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }

        return new PrincipalDetail(user, attributes);
    }

}
