package org.launchcode.controllers;

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
import javax.validation.Valid;
import java.util.*;

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

        Person person = personDao.findByEmail(email);
        List<Dog> dogs = person.getDogs();

        if (dogs.size() == 0) {
            model.addAttribute("err", "Please enter *Dog Details* first");
            return "service/add-service";
        }

        //model.addAttribute("error", "");
        model.addAttribute("dogs", dogs);
        model.addAttribute("service", new Service());
        model.addAttribute("person", person);
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

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Service");

            //model.addAttribute("error", "Invalid input - please reenter");
            if (!dogIds.equals("")) {
                for (int dogId : dogIds) {
                    Dog dog = dogDao.findById(dogId);
                    newService.setDog(dog);
                    if (dogId == 0) {
                        break;
                    }
                }

            }
            */
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Service");
            Person person = personDao.findByEmail(email);

            model.addAttribute("dogs", person.getDogs());
            model.addAttribute("person", personDao.findByEmail(email));

            return "service/add-service";
        }

        for (int dogId : dogIds) {

            Service service = new Service();
            Dog dog = dogDao.findById(dogId);

            service.setDog(dog);

            if (Radio.equals("weekly")) {
                service.setDayOfWeek(newService.getDayOfWeek());
            }

            Person person = personDao.findByEmail(email);

            service.setPerson(person);
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

    @RequestMapping(value = "list-services", method = RequestMethod.GET)
    public String displayListServicesForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person person = personDao.findByEmail(email);
        List<Service> services = serviceDao.findByPerson_Id(person.getId());
        model.addAttribute("person", person);
        model.addAttribute("services", services);

        return "service/list-services";
    }

    @RequestMapping(value = "remove-service/{id}", method = RequestMethod.GET)
    public String displayRemoveServiceForm(@PathVariable int id, Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {
        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        //Person pers = personDao.findByEmail(email);

        //List<Dog> dogs = pers.getDogs();

        //pers.setDogs(dogs);

        Service service = serviceDao.findById(id);

        //Dog dog = service.getDog();

        model.addAttribute("person", personDao.findByEmail(email));
        //model.addAttribute("dogs", dogDao.findAll());

        model.addAttribute("dogs", service.getDog());
        model.addAttribute("service", service);
        return "service/remove-services";
    }


    @RequestMapping(value = "remove-service/{id}", method = RequestMethod.POST)
    public String processRemoveDogDetailForm(@ModelAttribute @Valid Service service,
                                             @PathVariable int id, Person person, Model model) {

        //Person pers = personDao.findByEmail(person.getEmail());

      /*

        List<Dog> dogs = pers.getDogs();
        pers.setDogs(dogs);
        Dog dog = dogDao.findById(id);
        dog.setPerson(pers);
        */
        serviceDao.delete(service);
        return "redirect:/service/list-services";
    }

}
