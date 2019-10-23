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

import javax.persistence.criteria.CriteriaBuilder;
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
/*
    @RequestMapping(value = "")

    public String index(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        Person pers = personDao.findByEmail(email);
        List<Dog> dogs = pers.getDogs();

        model.addAttribute("dogs", dogs);
        model.addAttribute("title", "My Dogs");

        return "dogs/index";
    }
*/
    @RequestMapping(value = "add-dog-details", method = RequestMethod.GET)
    public String displayAddDogDetailsForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        Person person = personDao.findByEmail(email);

        List<Dog> dogs = person.getDogs();

        model.addAttribute("dogs", dogs);
        model.addAttribute("dog", new Dog());
        model.addAttribute("person", person);

        return "dogs/add-dog-details";
    }

    @RequestMapping(value = "add-dog-details", method = RequestMethod.POST)
    public String processAddDogDetailsForm(@ModelAttribute @Valid Dog newDog, Errors errors,
                                           @RequestParam("action") String action, String email, Person person, Model model) {

        // search dogDao if name input exists

        if (dogDao.findByName(newDog.getName()) != null) {
            model.addAttribute("error", "Duplicate dog name");
            model.addAttribute("title", "Add Dog");
            model.addAttribute("dogs", dogDao.findAll());
            return "dogs/add-dog-details";
        }
/*
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Dog");
            model.addAttribute("dogs", dogDao.findAll());
            return "dogs/add-dog-details";
        }
*/
        else {
            Person pers = personDao.findByEmail(person.getEmail());

            newDog.setPerson(pers);

            dogDao.save(newDog);

            model.addAttribute("dogs", pers.getDogs());
            model.addAttribute("person", pers);

            return "dogs/list-dog-details";

        }

    }

    @RequestMapping(value = "list-dog-details", method = RequestMethod.GET)
    public String displayListDogDetailsForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person pers = personDao.findByEmail(email);

        if (pers.getAdmin() == true) {
            return "person/as-owner";
        }
        List<Dog> dogs = pers.getDogs();

        model.addAttribute("dogs", dogs);
        model.addAttribute("person", pers);

        return "dogs/list-dog-details";
    }


    @RequestMapping(value = "edit-dog-details/{id}", method = RequestMethod.GET)
    public String displayEditDogDetailsForm(@PathVariable int id, Model model, @CookieValue(value = "person",
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
    public String processEditDogDetailsForm(@ModelAttribute @Valid Dog newDog, Errors errors,
                                            @PathVariable int id, Person person, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Dog");
            model.addAttribute("dog", dogDao.findById(id));
            return "dogs/edit-dog-details";
        }

        Person pers = personDao.findByEmail(person.getEmail());

        List<Dog> dogs = pers.getDogs();

        pers.setDogs(dogs);

        Dog dog = dogDao.findById(id);

        dog.setName(newDog.getName());
        dog.setBreed(newDog.getBreed());
        dog.setSize(newDog.getSize());
        dog.setSpecialNotes(newDog.getSpecialNotes());

        dog.setPerson(pers);

        dogDao.save(dog);

        model.addAttribute("dogs", pers.getDogs());
        model.addAttribute("person", pers);
        return "dogs/list-dog-details";

    }

    @RequestMapping(value = "remove-dog-details/{id}", method = RequestMethod.GET)
    public String displayRemoveDogDetailsForm(@PathVariable int id, Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person pers = personDao.findByEmail(email);

        model.addAttribute("person", personDao.findByEmail(email));
        //model.addAttribute("dogs", dogDao.findAll());

        model.addAttribute("dog", dogDao.findById(id));

        return "dogs/remove-dog-details";
    }


    @RequestMapping(value = "remove-dog-details/{id}", method = RequestMethod.POST)
    public String processRemoveDogDetailForm(@ModelAttribute @Valid Dog newDog, Errors errors,
                                             @PathVariable int id, Person person, Model model) {


        Person pers = personDao.findByEmail(person.getEmail());

        List<Dog> dogs = person.getDogs();

        //pers.setDogs(dogs);

        Dog dog = dogDao.findById(id);
        dog.setPerson(person);

        dogDao.delete(dog);

        model.addAttribute("dogs", pers.getDogs());
        model.addAttribute("person", pers);

        return "dogs/list-dog-details";
    }
}


