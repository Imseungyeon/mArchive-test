package syim.reviewboard.controller;

import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import syim.reviewboard.config.auth.PrincipalDetailService;
import syim.reviewboard.dto.ResponseDto;
import syim.reviewboard.model.User;
import syim.reviewboard.service.UserService;

@RestController
public class UserApiController {
    //injection 작업
    private final UserService userService;

    private final PrincipalDetailService principalDetailService;

    public UserApiController(PrincipalDetailService principalDetailService, UserService userService){
        this.principalDetailService = principalDetailService;
        this.userService = userService;
    }

    //1. save 메소드, 데이터는 post 형태로 받기 때문에 @RequestBody
    @PostMapping("/auth/joinProc")
    public ResponseDto<Boolean> save(@RequestBody User user){
        userService.signUp(user);
        //insert가 제대로 이루어졌다면 status = 200, data = true
        return new ResponseDto<>(HttpStatus.OK, true);
    }

    //2. update 메소드,
    @PutMapping("/user")
    public ResponseDto<Boolean> update(@RequestBody User user, HttpSession httpSession){
        userService.updateUserInfo(user);
        //업데이트한 정보를 spring security에서 관리하는 객체를 업데이트
        UserDetails currentUserDetails = principalDetailService.loadUserByUsername(user.getUsername());
        //UsernamePasswordAuthenticationToken라는 객체로 만들고 이 객체를 SecurityContextHolder에 셋팅
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUserDetails, currentUserDetails.getPassword(), currentUserDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        //세션에 "SPRING_SECURITY_CONTEXT"라는 이름으로 저장
        httpSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        //status=200, data = true 리턴
        return new ResponseDto<>(HttpStatus.OK, true);
    }

    //3. checkUserName 메소드
    @GetMapping("/auth/username/{userName}")
    public ResponseDto<Boolean> checkUserName(@PathVariable String userName){
        ResponseDto<Boolean> response = new ResponseDto<>(HttpStatus.OK, true);
        User selectedUser = userService.findUser(userName);
        if(selectedUser.getUsername() != null) {
            //회원가입 시 사용자 이름이 DB에 있는 값이라면 status = 200이지만 데이터는 false
            response = new ResponseDto<>(HttpStatus.OK, false);
        }
        //없는 값이라면 최초 셋팅했던 (HttpStatus.OK, true)로 리턴
        return response;
    }
}
