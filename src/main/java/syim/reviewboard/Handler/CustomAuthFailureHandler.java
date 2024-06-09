package syim.reviewboard.Handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
        String errorMessage = "";
        //exception이 BadCredentialsException이라면 아이디 또는 비밀번호가 일치하지 않는다는 메시지 맵핑
        if (exception instanceof BadCredentialsException){
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다. 다시 확인해 주세요.";
        }
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        setDefaultFailureUrl("/auth/loginFailure?error=true&exception=" + errorMessage);
        //부모의 onAuthenticationFailure을 호출하여 이후의 로직이 수행되도록
        super.onAuthenticationFailure(request, response, exception);
    }
}
