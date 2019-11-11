package org.launchcode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// @Controller annotation is an annotation used in Spring MVC framework
// (the component of Spring Framework used to implement Web Application).
// The @Controller annotation indicates that a particular class serves the role of a controller.
// This class perform the business logic (and can call the services) by its method.
// An @Controller typically would have a URL mapping and be triggered by a web request.
// Handles routing along with HTTP get and post requests
// The dispatcher scans the classes annotated with @Controller and detects @RequestMapping annotations within them.
@Controller
public class HomeController {

    // landing page/home page
    // @RequestMapping maps a url to a particular handler method
    @RequestMapping(value = "")
    public String index() {

        return "home/index";
    }
}
