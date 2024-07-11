package syim.reviewboard.config.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import syim.reviewboard.model.User;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetail implements UserDetails, OAuth2User {
    private User user;
    private Map<String, Object> attributes;

    //기본 로그인 시 사용할 생성자
    public PrincipalDetail(User user) {
        this.user = user;
    }

    //OAuth2 로그인 시 사용할 생성자
    public PrincipalDetail(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {
            return "ROLE_" + user.getRole();
        });
        return collectors;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public String getName() {
        // 필요한 경우 사용자 이름을 반환합니다. 예를 들어:
        return user.getUsername();
    }

    @Override
    public <A> A getAttribute(String name) {
        return (A) attributes.get(name);
    }
}
