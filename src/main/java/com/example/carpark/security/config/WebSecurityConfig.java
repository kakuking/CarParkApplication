package com.example.carpark.security.config;

import com.example.carpark.OAuth.CustomOauth2UserService;
import com.example.carpark.OAuth.OAuth2LoginSuccessHandler;
import com.example.carpark.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final CustomOauth2UserService oauth2UserService;

    @Autowired
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()

                    .antMatchers("/user/**")
                        .hasAnyRole("USER", "ADMIN")
                    .antMatchers("/admin/**")
                        .hasRole("ADMIN")
                    .antMatchers("/api/v*/registration/**")
                        .permitAll()
                    .antMatchers("/register")
                        .permitAll()
                    .antMatchers("/oauth2/**")
                        .permitAll()
                .anyRequest()
                .authenticated().and()
                    .formLogin()
                        .loginPage("/login")
                            .permitAll()
                .and()
                    .oauth2Login() //google and others login
                        .loginPage("/login")
                        .userInfoEndpoint().userService(oauth2UserService)
                        .and()
                        .successHandler(oAuth2LoginSuccessHandler)
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/accessDenied")
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);

        return provider;
    }


}
