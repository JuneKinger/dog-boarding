package org.launchcode.controllers;

import org.apache.naming.factory.SendMailFactory;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.launchcode.models.data.DogDao;
import org.launchcode.models.data.PersonDao;
import org.launchcode.models.forms.Dog;
import org.launchcode.models.forms.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Identity;
import java.util.List;
import java.util.Optional;
import java.lang.String;

@Controller
@RequestMapping(value = "person")
public class ProfileController {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private DogDao dogDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("person", personDao.findAll());
        return "person/index";
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String displaySignupForm(Model model) {

        model.addAttribute("person", new Person());

        return "person/signup";
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String processSignupForm(@ModelAttribute  @Valid Person person, Errors errors, String verify,
                                    String email, Model model, HttpServletResponse response) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Sign up");
            return "person/signup";
        }

        //String result = person.getFirstName().trim();
        if (person.getFirstName().trim().equals("") || (person.getLastName().trim().equals("")) ||
                (person.getEmail().trim().equals("")) || (person.getAddress().trim().equals("")) ||
                (person.getPassword().trim().equals("")) || (person.getHomePhone().trim().equals(""))) {
            model.addAttribute("error", "Invalid input - please re-enter");
            model.addAttribute("title", "Sign up");
            return "person/signup";
        }

        if (personDao.findByEmail(email) != null) {
            model.addAttribute("error", "This email address has been taken");
            model.addAttribute("title", "Sign up");
            return "person/signup";
        }

        if (person.getPassword().equals(verify) && verify != "") {

            Cookie cookie = new Cookie("person", person.getEmail());
            // set the path so the whole application has access to the cookie
            cookie.setPath("/");

            // method of HttpServletResponse interface is used to add cookie in response object
            response.addCookie(cookie);

            personDao.save(person);
            return "redirect:/dogs/add-dog-details";
        }
        else {


            // if password error, only password should be set to null. The rest is sent to the view with a refresh
            // of other fields
            model.addAttribute("firstname", person.getFirstName());
            model.addAttribute("lastname", person.getLastName());
            model.addAttribute("email", person.getEmail());
            model.addAttribute("address", person.getAddress());
            model.addAttribute("hPhone", person.getHomePhone());
            model.addAttribute("cPhone", person.getCellPhone());
            person.setPassword("");
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("title", "Sign up");

            return "person/signup";
        }

    }


    @RequestMapping(value = "login", method = RequestMethod.GET)

    public String displayLoginForm(Model model) {
        model.addAttribute("person", new Person());
        return "person/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLoginForm(Model model, @ModelAttribute  Person person, HttpServletResponse response) {

        Person pers = personDao.findByEmail(person.getEmail());
        if (pers == null || (!pers.getPassword().equals(person.getPassword()))) {
            model.addAttribute("error", "Invalid email and/or password");
            model.addAttribute("title", "Login");
            return "person/login";
       }
        // create cookie with cookie name and value (key-value pair) using Cookie class
        Cookie cookie = new Cookie("person", person.getEmail());
        // set the path so the whole application has access to the cookie
        cookie.setPath("/");

        // method of HttpServletResponse interface is used to add cookie in response object
        response.addCookie(cookie);

        model.addAttribute("title", pers.getFirstName());
        //return "redirect:/person/edit/" + person.getId();
        return "person/index";
    }


    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                c.setMaxAge(0);
                c.setPath("/");
                response.addCookie(c);
            }
        }
        /*
        // remove cookies from a browser by adding a new one to the response with the
        // same name but with maxAge value set to 0 and value = ""

        Cookie emailCookieRemove = new Cookie("email", "");
        emailCookieRemove.setMaxAge(0);;
        emailCookieRemove.setPath("/");
        response.addCookie(emailCookieRemove);
    */
        return "redirect:/person/login";
    }


    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String displayEditForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        Person pers = personDao.findByEmail(email);

        model.addAttribute("person", pers);

        return "person/edit";

    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(@ModelAttribute @Valid Person person, Errors errors,
                                  @RequestParam String email, Model model) {

        if (errors.hasErrors()) {
            return "person/edit";
        }

        if (person.getFirstName().trim().equals("") || (person.getLastName().trim().equals("")) ||
                (person.getAddress().trim().equals("")) || (person.getHomePhone().trim().equals(""))) {
            model.addAttribute("error", "Invalid input - please re-enter");
            model.addAttribute("title", "Edit");
            return "person/edit";
        }

        Person pers = personDao.findByEmail(email);
        pers.setFirstName(person.getFirstName());
        pers.setLastName(person.getLastName());
        pers.setAddress(person.getAddress());
        pers.setHomePhone(person.getHomePhone());
        pers.setCellPhone(person.getCellPhone());
        personDao.save(pers);

        return "person/index";
    }


    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none") || email.equals("")) {
            return "redirect:/person/login";
            //return "person/login";
        }

        Person pers = personDao.findByEmail(email);
        model.addAttribute("person", pers);

        List<Dog> dogs = pers.getDogs();

        return "person/remove";

    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveForm(HttpServletRequest request, HttpServletResponse response, @RequestParam String email) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                c.setMaxAge(0);
                c.setPath("/");
                response.addCookie(c);
            }
        }
        Person pers = personDao.findByEmail(email);
        personDao.delete(pers);

        return "person/index";
    }


}


