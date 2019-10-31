package org.launchcode.controllers;

import org.launchcode.models.data.PersonDao;
import org.launchcode.models.forms.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private PersonDao personDao;

    @RequestMapping(value = "")
    public String index() {

        return "home/index";
    }
}
