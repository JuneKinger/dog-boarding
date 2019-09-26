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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("dogs")
public class DogController {

    @Autowired
    private DogDao dogDao;

    @Autowired
    private PersonDao personDao;

    @RequestMapping(value = "")

    // takes an object out of the db using Spring Data JPA (cheeseDao),
    // run it through Spring MVC to Thymeleaf layer where we had a list of POJOs
    // where we rendered that list to the webpage.
    // method binds data (list of cheeses) to the model
    public String index(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        Person pers = personDao.findByEmail(email);
        List<Dog> dogs = pers.getDogs();

        // findAll() is an iterable that Thymeleaf uses in th:each="dog : ${dogs}"
        model.addAttribute("dogs", dogs);
        model.addAttribute("title", "My Dogs");

        return "dogs/index";
    }

    @RequestMapping(value = "add-dog-details", method = RequestMethod.GET)
    public String displayAddDogDetailForm(Model model, @CookieValue(value = "person",
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
    public String processAddDogDetailForm(@ModelAttribute @Valid Dog newDog, Errors errors,
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

    @RequestMapping(value = "list-dog-details", method = RequestMethod.GET)
    public String displayListDogDetailForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person pers = personDao.findByEmail(email);

        model.addAttribute("dogs", pers.getDogs());
        model.addAttribute("person", pers);

        return "dogs/list-dog-details";
    }



    @RequestMapping(value = "edit-dog-details/{id}", method = RequestMethod.GET)
    public String displayEditDogDetailForm(@PathVariable int id, Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person pers = personDao.findByEmail(email);

        model.addAttribute("person", personDao.findByEmail(email));
        //model.addAttribute("dogs", dogDao.findAll());

        model.addAttribute("dog", dogDao.findById(id));

        return "dogs/edit-dog-details";
    }


    @RequestMapping(value = "edit-dog-details/{id}", method = RequestMethod.POST)
    public String processDogDetailForm(@ModelAttribute @Valid Dog newDog, Errors errors,
                                       @PathVariable int id, Person person, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Dog");
            model.addAttribute("dog", dogDao.findById(id));
            return "dogs/edit-dog-details";
        }

        Person pers = personDao.findByEmail(person.getEmail());

        newDog.setPerson(pers);

        dogDao.save(newDog);

        //if (action.equals("Edit")) {
            model.addAttribute("dog", dogDao.findById(id));
            return "dogs/list-dog-details";


    }

}

