package com.example.carpark.OAuth;

import com.example.carpark.appuser.AppUser;
import com.example.carpark.appuser.AppUserRepository;
import com.example.carpark.appuser.AppUserService;
import com.example.carpark.appuser.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AppUserService appUserService;
//    private AppUserRepository appUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2User oauth2User = (CustomOauth2User) authentication.getPrincipal();
        String email = oauth2User.getEmail();
        String name = oauth2User.getName();



        appUserService.makeUser(email, name);

        response.sendRedirect("/");
    }
}
