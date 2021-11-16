package com.example.carpark.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class adminPageController {

    @GetMapping
    public String admin(){
        return "/admin.html";
    }
}
