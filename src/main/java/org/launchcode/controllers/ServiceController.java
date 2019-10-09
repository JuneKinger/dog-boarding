package org.launchcode.controllers;

import org.apache.tomcat.util.buf.StringUtils;
import org.launchcode.models.data.DogDao;
import org.launchcode.models.data.PersonDao;
import org.launchcode.models.data.ServiceDao;
import org.launchcode.models.forms.Dog;
import org.launchcode.models.forms.Person;
import org.launchcode.models.forms.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.naming.Context;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("service")
public class ServiceController {

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private DogDao dogDao;

        /*
    @RequestMapping(value = "")
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
        return "service/add-service";
    }
*/

    @RequestMapping(value = "add-service", method = RequestMethod.GET)
    public String displayAddServiceForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        Person pers = personDao.findByEmail(email);
        List<Dog> dogs = pers.getDogs();

        if (dogs.size() == 0) {
            model.addAttribute("err", "Please enter *Dog Details* first");
            return "service/add-service";
        }

        //model.addAttribute("error", "");
        model.addAttribute("dogs", dogs);
        model.addAttribute("service", new Service());
        model.addAttribute("person", pers);
        return "service/add-service";
    }

    @RequestMapping(value = "add-service", method = RequestMethod.POST)
    public String processAddServiceForm(@ModelAttribute @Valid Service newService, Errors errors,
                                        @RequestParam("action") String action, int[] dogIds, String email, String Radio, String dayOfWeek, Model model) {

/*
      if (Radio.equals("weekly") && !dayOfWeek.matches("[A-Za-z]")) {
          model.addAttribute("errors", "Please enter Days of the Week");
          Person pers = personDao.findByEmail(personEmail);
          List<Dog> dogs = pers.getDogs();
          model.addAttribute("dogs", dogs);
          model.addAttribute("person", pers);
          return "service/add-service";
          }
        if (dogIds == null) {
            model.addAttribute("errors", "Please check mark at least one dog");
            return "service/add-service";
        }
*/
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Service");

            //model.addAttribute("error", "Invalid input - please reenter");

            Person pers = personDao.findByEmail(email);
            List<Dog> dogs = pers.getDogs();

            model.addAttribute("dogs", pers.getDogs());
            model.addAttribute("person", pers);
            return "service/add-service";
        }


        for (int dogId : dogIds) {

            Service service = new Service();
            service.setDog(dogDao.findById(dogId));

            if (Radio.equals("weekly")) {
                service.setDayOfWeek(newService.getDayOfWeek());
            }

            service.setPerson(personDao.findByEmail(email));
            service.setStartDate(newService.getStartDate());
            service.setEndDate(newService.getEndDate());

            serviceDao.save(service);
        }
        if (action.equals("Add")) {
            return "redirect:/service/add-service";
        } else {

            return "person/index";
        }

    }
/*
    @RequestMapping(value = "remove-service/{id}", method = RequestMethod.GET)
    public String displayRemoveDogDetailsForm(@PathVariable int id, Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {
        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Service service = serviceDao.findById(id);
        //List<Dog> dogs = service.getDog();
        //pers.setDogs(dogs);
        //Dog dog = dogDao.findById(id);
        //model.addAttribute("person", personDao.findByEmail(email));
        //model.addAttribute("dogs", dogDao.findAll());
        model.addAttribute("service", serviceDao.findById(id));
        return "service/remove";
    }
    @RequestMapping(value = "remove-service/{id}", method = RequestMethod.POST)
    public String processRemoveDogDetailForm(@ModelAttribute @Valid Dog newDog, Errors errors,
                                             @PathVariable int id, Person person, Model model) {
        Person pers = personDao.findByEmail(person.getEmail());
        List<Dog> dogs = pers.getDogs();
        pers.setDogs(dogs);
        Dog dog = dogDao.findById(id);
        dog.setPerson(pers);
        dogDao.delete(dog);
        model.addAttribute("dogs", pers.getDogs());
        model.addAttribute("person", pers);
        return "dogs/list-dog-details";
    }
 */
}
