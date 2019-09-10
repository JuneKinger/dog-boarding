package org.launchcode.controllers;

import org.launchcode.models.data.PersonDao;
import org.launchcode.models.forms.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "person")
public class PersonController {

    @Autowired
    private PersonDao personDao;

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String displaySignupForm(Model model) {
        model.addAttribute("person", new Person());
        return "signup";
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String processSignupForm(@ModelAttribute  @Valid Person person, Errors errors, String verify, String email, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Sign up");
            return "signup";
        }

        if (personDao.findByEmail(email) != null) {
            model.addAttribute("error", "This email address has been taken");
            return "signup";
        }

        if (person.getPassword().equals(verify) && verify != "") {
            personDao.save(person);
            return "index";
        }
        else {
            // if password error, only password should be set to null. The rest is sent to the view with a refresh
            // of other fields
            model.addAttribute("firstname", person.getFirstName());
            model.addAttribute("lastname", person.getLastName());
            model.addAttribute("email", person.getEmail());
            model.addAttribute("address", person.getAddress());
            model.addAttribute("phone", person.getPhone());
            person.setPassword("");
            model.addAttribute("error", "Passwords do not match");

            return "signup";
        }

    }
    @RequestMapping(value = "login", method = RequestMethod.GET)

    public String displayLoginForm(Model model) {
        model.addAttribute(new Person());
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLoginForm(Model model, @ModelAttribute  Person person, HttpServletResponse response) {
        Person pers = personDao.findByEmail(person.getEmail());
        if (pers == null || (!pers.getPassword().equals(pers.getPassword()))) {
            model.addAttribute("error", "Invalid email and/or password");
            model.addAttribute("title", "Login");
            return "login";
       }

        // create cookie with cookie name and value (key-value pair) using Cookie class
        Cookie cookie = new Cookie("person", person.getEmail());
        // set the path so the whole application has access to the cookie
        cookie.setPath("/");

        // method of HttpServletResponse interface is used to add cookie in response object
        response.addCookie(cookie);
        return "index";
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        // remove cookies from a browser by adding a new one to the response with the
        // same name but with maxAge value set to 0 and value = ""
        Cookie emailCookieRemove = new Cookie("email", "");
        emailCookieRemove.setMaxAge(0);
        response.addCookie(emailCookieRemove);
        return "index";
    }

}


