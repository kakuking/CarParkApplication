package com.example.carpark.appuser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "AppUser_Sequence",
                       sequenceName = "AppUser_Sequence",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "AppUser_Sequence")
    private Long id;

    //user info
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private String name;
    private String address;
    private String carRegNum;

    //auth provider
    @Enumerated(EnumType.STRING)
    private AuthenticationProvider auth_provider;

    //locked and enabled
    private Boolean locked = false;
    private Boolean enabled = false;



    public AppUser(String name
            , String email
            , String password
            , AppUserRole appUserRole) {
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;

        this.name = name;
    }

    public AppUser(String name,
                   String email,
                   String password,
                   AppUserRole appUserRole,
                   Boolean locked,
                   Boolean enabled) {
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
        this.locked = locked;
        this.enabled = enabled;

        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getAddress(){
        return this.address;
    }

}
