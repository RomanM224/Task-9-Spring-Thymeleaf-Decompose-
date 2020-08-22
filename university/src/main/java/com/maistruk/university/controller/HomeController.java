package com.maistruk.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.maistruk.university.service.DatabaseInitializer;

@Controller
public class HomeController {

    DatabaseInitializer databaseInitializer;

    public HomeController(DatabaseInitializer databaseInitializer) {
        this.databaseInitializer = databaseInitializer;
    }

    @RequestMapping(value = "/")
    public ModelAndView home(Model model) {
        return new ModelAndView("home");
    }

    @RequestMapping(value = "/generateInfo")
    public ModelAndView generateInfo() {
        databaseInitializer.init();
        return new ModelAndView("home");
    }

}
