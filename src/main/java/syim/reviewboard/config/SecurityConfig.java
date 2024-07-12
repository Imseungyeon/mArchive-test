package syim.reviewboard.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import syim.reviewboard.Handler.CustomAuthFailureHandler;
import syim.reviewboard.config.auth.PrincipalDetailService;
import syim.reviewboard.service.CustomOAuth2UserService;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api.naver.client.id}")
    private String naverClientId;
    @Value("${api.naver.client.secret}")
    private String naverClientSecret;
    @Value("${api.naver.redirect.uri}")
    private String naverRedirectUri;
    @Value("${api.kakao.client.id}")
    private String kakaoClientId;
    @Value("${api.kakao.client.secret}")
    private String kakaoClientSecret;
    @Value("${api.kakao.redirect.uri}")
    private String kakaoRedirectUri;

    private final AuthenticationFailureHandler customFailureHandler;
    private final PrincipalDetailService principalDetailService;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(AuthenticationFailureHandler customFailureHandler, PrincipalDetailService principalDetailService, CustomOAuth2UserService customOAuth2UserService) {
        this.customFailureHandler = customFailureHandler;
        this.principalDetailService = principalDetailService;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    private static final String[] PERMIT_URL_ARRAY = {
            "/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**", "/h2-console/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                            .requestMatchers(
                                    new AntPathRequestMatcher("/"),
                                    new AntPathRequestMatcher("/auth/**"),
                                    new AntPathRequestMatcher("/js/**"),
                                    new AntPathRequestMatcher("/css/**"),
                                    new AntPathRequestMatcher("/image/**"),
                                    new AntPathRequestMatcher("/dummy/**"),
                                    new AntPathRequestMatcher("/h2-console/**")
                            )
                            .permitAll()// 특정 URL 허용
                            .anyRequest().authenticated() // 이외 모든 요청은 인증 필요
                )
                .formLogin(formLogin ->
                        formLogin.loginPage("/auth/loginForm") // 로그인 페이지
                                 .loginProcessingUrl("/auth/loginProc") // 로그인 처리 URL
                                 .defaultSuccessUrl("/", true) // 로그인 성공 시 기본 URL
                                 .failureHandler(customFailureHandler) // 로그인 실패 핸들러
                                 .permitAll()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/auth/loginForm")
                                .defaultSuccessUrl("/", true)
                                .failureHandler(customFailureHandler).permitAll()
                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService)
                                )
                )
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())// 로그아웃 설정
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // h2 db 웹 콘솔 접근 설정
                .userDetailsService(principalDetailService); // 사용자 세부 서비스 설정

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                kakaoClientRegistration(),
                naverClientRegistration()
        );
    }

    private ClientRegistration naverClientRegistration() {
        return ClientRegistration.withRegistrationId("naver")
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .scope("profile", "email")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .clientName("Naver")
                .redirectUri(naverRedirectUri)
                .build();
    }

    private ClientRegistration kakaoClientRegistration() {
        System.out.println("kakaoclientregistration called");
        return ClientRegistration.withRegistrationId("kakao")
                .clientId(kakaoClientId)
                .clientSecret(kakaoClientSecret)
                .scope("profile_nickname", "account_email")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .clientName("Kakao")
                .redirectUri(kakaoRedirectUri)
                .build();
    }
}
