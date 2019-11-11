package org.launchcode.controllers;

import org.launchcode.models.data.DogDao;
import org.launchcode.models.data.PersonDao;
import org.launchcode.models.data.ServiceDao;
import org.launchcode.models.forms.Dog;
import org.launchcode.models.forms.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.lang.String;
import static jdk.nashorn.internal.objects.NativeString.trim;

@Controller
@RequestMapping("dogs")
public class DogController {

    @Autowired
    private DogDao dogDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ServiceDao serviceDao;

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
    public String processAddDogDetailsForm(@ModelAttribute @Valid Dog newDog, Person person, Model model) {

        Person pers = personDao.findByEmail(person.getEmail());

        List<Dog> dogs = pers.getDogs();

        for (int i = 0; i < dogs.size(); i++) {

            if (dogs.get(i).getName().equals(newDog.getName())) {
                // search dogDao if name input exists
                model.addAttribute("error", "Duplicate dog name");
                model.addAttribute("title", "Add Dog");

                return "dogs/add-dog-details";
            }

        }

        if (trim(newDog.getName()).equals("")) {

            model.addAttribute("error", "Name cannot be blank - please re-enter");
            model.addAttribute("title", "add-dog");
            return "dogs/add-dog-details";
        }

        newDog.setPerson(pers);

        dogDao.save(newDog);

        model.addAttribute("dogs", pers.getDogs());
        model.addAttribute("person", pers);

        return "redirect:/dogs/list-dog-details";
    }


    @RequestMapping(value = "list-dog-details", method = RequestMethod.GET)
    public String displayListDogDetailsForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person pers = personDao.findByEmail(email);

        if (pers.getAdmin() == true) {
            String mess = "Please register / log in as owner first!";
            model.addAttribute("mess", mess);
            return "person/mess";
        }
        List<Dog> dogs = pers.getDogs();

        model.addAttribute("dogs", dogs);
        model.addAttribute("person", pers);

        return "dogs/list-dog-details";
    }

    // edit dog-details from list of dogs displayed via list-dog-details - note the id selected
    @RequestMapping(value = "edit-dog-details/{id}", method = RequestMethod.GET)
    public String displayEditDogDetailsForm(@PathVariable int id, Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        model.addAttribute("person", personDao.findByEmail(email));
        model.addAttribute("dog", dogDao.findById(id));

        return "dogs/edit-dog-details";
    }


    @RequestMapping(value = "edit-dog-details/{id}", method = RequestMethod.POST)
    public String processEditDogDetailsForm(@ModelAttribute @Valid Dog newDog, Person person,
                                            @PathVariable int id, Model model) {

        if (trim(newDog.getName()).equals("")) {

            model.addAttribute("error", "Name cannot be blank - please re-enter");
            model.addAttribute("title", "edit-dog");
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

        model.addAttribute("person", personDao.findByEmail(email));
        model.addAttribute("dog", dogDao.findById(id));
        model.addAttribute("warn", "Please note - Deleting a dog will also delete services if present");

        return "dogs/remove-dog-details";
    }


    @RequestMapping(value = "remove-dog-details/{id}", method = RequestMethod.POST)
    public String processRemoveDogDetailForm(@PathVariable int id, Person person, Model model) {

        Person pers = personDao.findByEmail(person.getEmail());

        List<Dog> dogs = person.getDogs();

        Dog dog = dogDao.findById(id);
        dog.setPerson(person);

        dogDao.delete(dog);

        model.addAttribute("dogs", pers.getDogs());
        model.addAttribute("person", pers);

        return "dogs/list-dog-details";
    }
}
