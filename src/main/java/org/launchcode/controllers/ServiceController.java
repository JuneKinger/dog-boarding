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

    @RequestMapping(value = "add-service", method = RequestMethod.GET)
    public String displayAddServiceForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }

        Person person = personDao.findByEmail(email);

        if (person.getAdmin() == true) {
            return "person/as-owner";
        }

        List<Dog> dogs = person.getDogs();

        if (dogs.size() == 0) {
            return  "redirect:/service/error-services";
        }

        model.addAttribute("dogs", dogs);
        model.addAttribute("service", new Service());
        model.addAttribute("person", person);
        return "service/add-service";
    }


    @RequestMapping(value = "add-service", method = RequestMethod.POST)
    public String processAddServiceForm(@ModelAttribute @Valid Service newService, Errors errors,
                                        @RequestParam int[] dogIds, String email, String Radio, String dayOfWeek, Model model) {

        Person person = personDao.findByEmail(email);
        if (person.getAdmin() == true) {
            String mess = "Please register / log in as owner first!";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

        if (newService.getStartDate().after(newService.getEndDate())) {
            model.addAttribute("error", "Drop off date must be <= Pick up date");
            model.addAttribute("dogs", person.getDogs());
            model.addAttribute("person", personDao.findByEmail(email));
            return "service/add-service";
        }
        Date today = new Date();
        long dt = System.currentTimeMillis() - 1000*60*60*24;
        Date beforeDate = new Date(dt);

        if (newService.getStartDate().before(beforeDate)) {
            model.addAttribute("error", "Drop off date must be >= today's date - please re-enter!");
            model.addAttribute("dogs", person.getDogs());
            model.addAttribute("person", personDao.findByEmail(email));
            return "service/add-service";
        }

        if (newService.getEndDate().before(beforeDate)) {
            model.addAttribute("error", "Pick up date must be >= today's date - please re-enter!");
            model.addAttribute("dogs", person.getDogs());
            model.addAttribute("person", personDao.findByEmail(email));
            return "service/add-service";
        }

        for (int dogId : dogIds) {

            Service service = new Service();
            Dog dog = dogDao.findById(dogId);

            service.setDog(dog);
            service.setPerson(person);
            service.setStartDate(newService.getStartDate());
            service.setEndDate(newService.getEndDate());

            serviceDao.save(service);
        }

        return "redirect:/service/add-service";

    }
    @RequestMapping(value = "error-services", method = RequestMethod.GET)
    public String displayError(Model model)  {
        model.addAttribute("err", "Please enter *Dog Details* first");
        return "service/error-services";

    }

    @RequestMapping(value = "list-services", method = RequestMethod.GET)
    public String displayListServicesForm(Model model, @CookieValue(value = "person",
            defaultValue = "none") String email) {

        if (email.equals("none")) {
            return "redirect:/person/login";
        }
        Person person = personDao.findByEmail(email);

        if (person.getAdmin() == true) {
            String mess = "Please register / log in as owner first!";
            model.addAttribute("mess", mess);
            return "person/mess";
        }

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

        Service service = serviceDao.findById(id);

        model.addAttribute("person", personDao.findByEmail(email));
        model.addAttribute("dogs", service.getDog());
        model.addAttribute("service", service);
        return "service/remove-services";
    }


    @RequestMapping(value = "remove-service/{id}", method = RequestMethod.POST)
    public String processRemoveDogDetailForm(@ModelAttribute @Valid Service service,
                                             @PathVariable int id) {

        serviceDao.delete(service);
        return "redirect:/service/list-services";
    }

}