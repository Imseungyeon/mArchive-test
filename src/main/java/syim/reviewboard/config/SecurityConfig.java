package syim.reviewboard.config;

import jakarta.servlet.DispatcherType;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import syim.reviewboard.Handler.CustomAuthFailureHandler;
import syim.reviewboard.config.auth.PrincipalDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationFailureHandler customFailureHandler;
    //private final CustomAuthFailureHandler customFailureHandler;
    private final PrincipalDetailService principalDetailService;

    public SecurityConfig(AuthenticationFailureHandler customFailureHandler, PrincipalDetailService principalDetailService) {
        this.customFailureHandler = customFailureHandler;
        this.principalDetailService = principalDetailService;
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
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))// 로그아웃 설정
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
}
