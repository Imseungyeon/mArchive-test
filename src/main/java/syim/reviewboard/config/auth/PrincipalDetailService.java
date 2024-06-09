package syim.reviewboard.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import syim.reviewboard.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import syim.reviewboard.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService {
    //해당 username이 DB에 있는지 확인
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다." + username));
        return new PrincipalDetail(principal); // 매개변수로 넘겨주어 security 세션에 저장
    }
}
