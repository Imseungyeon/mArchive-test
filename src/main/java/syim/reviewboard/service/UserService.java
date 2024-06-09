package syim.reviewboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syim.reviewboard.model.LoginType;
import syim.reviewboard.model.UserRole;
import syim.reviewboard.repository.UserRepository;
import syim.reviewboard.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encode;

    //3가지 메소드 작성 필요 (signUp, findUser, updateUserInfo)

    //1. signUp - 회원가입
    @Transactional
    public void signUp(User user){
        String rawPassword = user.getPassword();
        String encodePassword = encode.encode(rawPassword); //비밀번호 암호화
        user.setPassword(encodePassword);
        user.setLoginType(LoginType.GENERAL);
        user.setRole(UserRole.USER);
        userRepository.save(user); //DB에 insert
    }

    //2. findUser
    @Transactional(readOnly = true)
    public User findUser(String username){
        User user = userRepository.findByUsername(username).orElseGet(User::new); //사용자 이름으로 객체 가져오기
        return user;
    }

    //3. updateUserInfo
    @Transactional
    public void updateUserInfo(User user){
        long id = user.getId();
        //id를 이용하여 현재 사용자의 정보 가져오기
        User currentUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID 사용자를 찾을 수 없습니다."));

        //로그인 타입이 자체 로그인(GENERAL)인지
        if(currentUser.getLoginType() == LoginType.GENERAL){
            String rawPassword = user.getPassword();
            String encodePassword = encode.encode(rawPassword);
            currentUser.setPassword(encodePassword);
            currentUser.setEmail(user.getEmail());
        }
    }
}
