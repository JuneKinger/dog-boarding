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
import java.text.SimpleDateFormat;
import java.text.ParseException;
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

    @RequestMapping(value = "add-services", method = RequestMethod.GET)
    public String displayAddServiceForm(Model model, @CookieValue(value = "person",
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

        List<Dog> dogs = person.getDogs();

        if (dogs.size() == 0) {
            return  "redirect:/service/checkbox-error-services";
        }

        model.addAttribute("dogs", dogs);
        model.addAttribute("service", new Service());
        model.addAttribute("person", person);
        return "service/add-services";
    }


    @RequestMapping(value = "add-services", method = RequestMethod.POST)
    public String processAddServiceForm(@ModelAttribute @Valid Service newService,
                                        @RequestParam int[] dogIds, String email, Model model) {

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
            return "service/add-services";
        }

        //long nowDt = System.currentTimeMillis();
        long dt = System.currentTimeMillis() - 1000*60*60*24;
        Date beforeDate = new Date(dt);

        if (newService.getStartDate().before(beforeDate)) {
            model.addAttribute("error", "Drop off date must be >= today's date - please re-enter!");
            model.addAttribute("dogs", person.getDogs());
            model.addAttribute("person", personDao.findByEmail(email));
            return "service/add-services";
        }

        if (newService.getEndDate().before(beforeDate)) {
            model.addAttribute("error", "Pick up date must be >= today's date - please re-enter!");
            model.addAttribute("dogs", person.getDogs());
            model.addAttribute("person", personDao.findByEmail(email));
            return "service/add-services";
        }

        for (int dogId : dogIds) {

            Dog dogRec = dogDao.findById(dogId);
            List<Service> services = dogRec.getServices();

            for (int j = 0; j < services.size(); j++) {

               if ((newService.getStartDate().equals(services.get(j).getStartDate()) ||
                       newService.getStartDate().after(services.get(j).getStartDate())) &&
                (newService.getStartDate().before(services.get(j).getEndDate())) ||
                       (newService.getStartDate().equals(services.get(j).getEndDate())))
            {
                model.addAttribute("error", "Duplicate service - please re-enter");
                model.addAttribute("title", "Add Service");
                model.addAttribute("dogs", person.getDogs());
                model.addAttribute("person", personDao.findByEmail(email));
                return "service/add-services";
               }

            }

            Service service = new Service();
            //Dog dog = dogDao.findById(dogId);

            service.setDog(dogRec);
            service.setPerson(person);
            service.setStartDate(newService.getStartDate());
            service.setEndDate(newService.getEndDate());

            serviceDao.save(service);
        }

        return "redirect:/service/add-services";

    }


    @RequestMapping(value = "checkbox-error-services", method = RequestMethod.GET)
    public String displayError(Model model)  {
        model.addAttribute("err", "Please enter *Dog Details* first");
        return "service/checkbox-error-services";

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

        //List<Service> sortServices = serviceDao.findAllByStartDateOrderByStartDateAscNative();
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