package com.example.carpark.appuser;

import com.example.carpark.registration.token.ConfirmationToken;
import com.example.carpark.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    @Autowired
    private final AppUserRepository appUserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    private final static String USER_NOT_FOUND = "User with email %s not found";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepository
                            .findByEmail(appUser.getEmail())
                            .isPresent();
        if(userExists){
            throw new IllegalStateException("Email already in use");
        }

        appUser.setAuth_provider(AuthenticationProvider.LOCAL);

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }


    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }


    public void makeUser(String email, String name) {

        boolean userExists = appUserRepository
                .findByEmail(email)
                .isPresent();

        if(!userExists){
            AppUser appUser = new AppUser();
            appUser.setEmail(email);
            appUser.setName(name);
            appUser.setAuth_provider(AuthenticationProvider.GOOGLE);
            appUser.setEnabled(true);
            appUser.setLocked(false);

            appUserRepository.save(appUser);
        }




    }

    public void updateUser(String input, AppUser user, String whatToChange) {
        if(whatToChange == "name"){
            user.setName(input);
            appUserRepository.updateName(input, user.getEmail());
        } else if(whatToChange == "address"){
            user.setAddress(input);
            appUserRepository.updateAddress(input, user.getEmail());
        }
    }
}
