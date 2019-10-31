package org.launchcode.controllers;

import org.launchcode.models.data.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private PersonDao personDao;

    @RequestMapping(value = "")
    // bind this method parameter to a cookie. Retrieve a cookie using @CookieValue
    public String index() {

        return "home/index";
    }
}
