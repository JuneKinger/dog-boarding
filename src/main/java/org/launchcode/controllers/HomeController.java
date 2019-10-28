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
    // bind this method parameter to a cookie. Retrieve a cookie using @CookieValue
    public String index(Model model, @CookieValue(value="person", defaultValue = "none") String email) {

        // if no cookies present, direct to login page to force a log in
        if (email.equals("none") || email.equals("")) {
            return "home/index";
        }

         Person person = personDao.findByEmail(email);
         model.addAttribute("person", person.getFirstName());

        return "home/index";
    }
}
