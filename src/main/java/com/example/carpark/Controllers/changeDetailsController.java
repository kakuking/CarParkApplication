package com.example.carpark.Controllers;


import com.example.carpark.appuser.AppUser;
import com.example.carpark.appuser.AppUserService;
import com.example.carpark.appuser.AuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/changeDetails")
public class changeDetailsController {
    private final AppUserService appUserService;

    @PostMapping("/name")
    public String changeFirstname(@RequestParam("name") String name,@AuthenticationPrincipal AppUser user){
        if(user != null){
            appUserService.updateUser(name, user, "name");
            return "/index.html";
        } else {
            return "/errorChangingDetailsGoogleLogin.html";
        }
    }

    @PostMapping("/address")
    public String changeAddress(@RequestParam("address") String address,AppUser user){
        if(user != null){
            appUserService.updateUser(address, user, "address");
            return "/index.html";
        } else {
            return "/errorChangingDetailsGoogleLogin.html";
        }
    }
}
