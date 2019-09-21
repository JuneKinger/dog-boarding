package org.launchcode.controllers;

import org.launchcode.models.data.DogDao;
import org.launchcode.models.data.PersonDao;
import org.launchcode.models.forms.Dog;
import org.launchcode.models.forms.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("dogs")
public class DogController {

    @Autowired
    private DogDao dogDao;

    @Autowired
    private PersonDao personDao;

    /*
    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("dogs", dogDao.findAll());
        model.addAttribute("title", "My dogs");
        return "dogs/index";
    }
*/

    @RequestMapping(value = "add-dog-details", method = RequestMethod.GET)
    public String displayDogDetailForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        model.addAttribute("dogs", dogDao.findAll());
        model.addAttribute("dog", new Dog());
        model.addAttribute("person", personDao.findByEmail(email));

        return "dogs/add-dog-details";
    }

    @RequestMapping(value = "add-dog-details", method = RequestMethod.POST)
    public String processDogDetailForm(@ModelAttribute @Valid Dog newDog, Errors errors,
                                       @RequestParam("action") String action, Person person, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Dog");
            model.addAttribute("dogs", dogDao.findAll());
            return "dogs/add-dog-details";
        }

        Person pers = personDao.findByEmail(person.getEmail());

        newDog.setPerson(pers);

        dogDao.save(newDog);

        if (action.equals("Add")) {
            model.addAttribute("dog", new Dog());
            return "dogs/add-dog-details";
        }
        else {

            return "person/index";
        }
    }

}

