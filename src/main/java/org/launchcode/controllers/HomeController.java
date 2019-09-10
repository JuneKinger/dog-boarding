package org.launchcode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "")

    // bind this method parameter to a cookie. Retrieve a cookie using @CookieValue
    public String index(@CookieValue(value="person", defaultValue = "none") String email) {

        // if no cookies present, direct to login page to force a log in
        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        return "index";
    }
}
