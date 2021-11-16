package com.example.carpark.Controllers;

import com.example.carpark.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class PageController {

    private final RegistrationService registrationService;

    @EventListener(ApplicationReadyEvent.class)
    public void addAdmin(){
        registrationService.registerAdmin();
    }


    @GetMapping("/user")
    public String user(){
        return "/user.html";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        return "/accessDenied.html";
    }

    @GetMapping("/register")
    public String register(){
        return "/register.html";
    }
}
