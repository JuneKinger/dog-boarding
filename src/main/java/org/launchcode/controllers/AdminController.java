package org.launchcode.controllers;

import org.launchcode.models.data.DogDao;
import org.launchcode.models.data.PersonDao;
import org.launchcode.models.data.ServiceDao;
import org.launchcode.models.forms.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private DogDao dogDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ServiceDao serviceDao;

    @RequestMapping(value = "list-owners", method = RequestMethod.GET)
    public String displayListDogDetailsForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person pers = personDao.findByEmail(email);

        // if owner tries to access admin, error message
        if (pers.getAdmin() == false) {
            String mess = "Access Denied!";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

        // A single row in person table will always be present for admin login/password
        if (personDao.count() == 1) {
            String mess = "No owners registered as yet!";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

        model.addAttribute("person", personDao.findAll());
        model.addAttribute("dogs", dogDao.findAll());
        return "admin/list-owners";

    }

    // list all boarders by drop-off date
    @RequestMapping(value = "list-all-boarders-drop-off", method = RequestMethod.GET)
    public String displayListAllBoardersDropOffForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person person = personDao.findByEmail(email);

        if (person.getAdmin() == false) {
            String mess = "Access Denied";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

        model.addAttribute("person", person);
        model.addAttribute("title", "All Boarders - By drop-off date");
        model.addAttribute("services", serviceDao. findAllOrderByStartDateAscNative());

        return "admin/list-all-boarders";
    }

    // list all boarders by pick-up date
    @RequestMapping(value = "list-all-boarders-pick-up", method = RequestMethod.GET)
    public String displayListAllBoardersPickUpForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person person = personDao.findByEmail(email);

        if (person.getAdmin() == false) {
            String mess = "Access Denied";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

        model.addAttribute("person", person);
        model.addAttribute("title", "All Boarders - By pick-up date");
        model.addAttribute("services", serviceDao. findAllOrderByEndDateAscNative());

        return "admin/list-all-boarders";
    }

    // list all future boarders by drop-off date
    @RequestMapping(value = "list-future-boarders-drop-off", method = RequestMethod.GET)
    public String displayListFutureDropOffForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person person = personDao.findByEmail(email);

        if (person.getAdmin() == false) {
            String mess = "Access Denied";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

        model.addAttribute("title", "Future Boarders - by drop off date");
        model.addAttribute("person", person);
        model.addAttribute("services", serviceDao.findAllByStartDateOrderByStartDateAscNative());

        return "admin/list-future-boarders";
    }


    // list all future boarders by pick-up date
    @RequestMapping(value = "list-future-boarders-pick-up", method = RequestMethod.GET)
    public String displayListFuturePickUpForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person person = personDao.findByEmail(email);

        if (person.getAdmin() == false) {
            String mess = "Access Denied";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

        model.addAttribute("title", "Future Boarders - By pick-up date");
        model.addAttribute("person", person);
        model.addAttribute("services", serviceDao.findAllByEndDateOrderByEndDateAscNative());

        return "admin/list-future-boarders";
    }
}


