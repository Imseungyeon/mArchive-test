package syim.reviewboard.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import syim.reviewboard.config.auth.PrincipalDetail;
import syim.reviewboard.error.DuplicateUserException;
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
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();

        //providerId, username, email 셋팅
        String providerId = null;
        String name = null;
        String username = null;
        String email = null;

        //username이 unique이므로 username에 email 셋팅
        //User 모델의 email에도 email 셋팅
        if ("naver".equals(registrationId)) {
            attributes = (Map<String, Object>) attributes.get("response");
            providerId = attributes.get("id").toString();
            name = attributes.get("name").toString();
            username = attributes.get("email").toString();
            email = attributes.get("email").toString();

        } else if ("kakao".equals(registrationId)) {
            providerId = attributes.get("id").toString();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            name = profile.get("nickname").toString();
            username = kakaoAccount.get("email").toString();
            email = kakaoAccount.get("email").toString();
        }
        User user = userRepository.findByProviderAndProviderId(registrationId, providerId);

        if (user == null) {
            //중복되는 username 확인 -> 없다면 null
            user = userRepository.findByUsername(username).orElse(null);
        }
        if (user == null) {
            //중복되지 않았음을 확인했기 때문에 새로운 User로 추가
            user = new User();
            user.setProvider(registrationId);
            user.setProviderId(providerId);
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setLoginType(LoginType.valueOf(registrationId.toUpperCase()));
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }
        return new PrincipalDetail(user, attributes);
    }

}
