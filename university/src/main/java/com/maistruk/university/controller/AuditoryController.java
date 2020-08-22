package com.maistruk.university.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.maistruk.university.exceptions.AuditoryException;
import com.maistruk.university.model.Auditory;
import com.maistruk.university.service.AuditoryService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/auditory")
public class AuditoryController {

    AuditoryService auditoryService;

    public AuditoryController(AuditoryService auditoryService) {
        this.auditoryService = auditoryService;
    }

    @GetMapping(value = "/showAll")
    public ModelAndView showAuditories() {
        ModelAndView modelAndView = new ModelAndView("/auditory/showAll");
        List<Auditory> auditories = auditoryService.getAll();
        return modelAndView.addObject("auditories", auditories);
    }

    @GetMapping(value = "/getById")
    public ModelAndView getById() {
        return new ModelAndView("/auditory/getById");
    }

    @RequestMapping("/getById")
    public ModelAndView showById(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("/auditory/showAll");
        Auditory auditory = auditoryService.getById(id);
        if (auditory == null) {
            return modelAndView.addObject("auditories", emptyList());
        }
        List<Auditory> auditories = asList(auditory);
        return modelAndView.addObject("auditories", auditories);
    }
    
    @GetMapping(value = "/create")
    public ModelAndView create() {
        return new ModelAndView("/auditory/create");
    }
    
    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("number") Integer number) {
        try {
            Auditory auditory = Auditory.builder().number(number).build();
            auditoryService.create(auditory);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Auditory created");
        } catch (AuditoryException exception) {
            ModelAndView modelAndView = new ModelAndView("/auditory/create");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/update")
    public ModelAndView update() {
        return new ModelAndView("/auditory/update");
    }
    
    @RequestMapping("/update")
    public ModelAndView update(@RequestParam("id") Integer id, @RequestParam("number") Integer number) {
        try {
            Auditory auditory = Auditory.builder().id(id).number(number).build();
            auditoryService.update(auditory);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Auditory updated");
        } catch (AuditoryException exception) {
            ModelAndView modelAndView = new ModelAndView("/auditory/update");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/delete")
    public ModelAndView delete() {
        return new ModelAndView("/auditory/delete");
    }
    
    @RequestMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Integer id) {
        try {
            auditoryService.delete(id);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Auditory deleted");
        } catch (AuditoryException exception) {
            ModelAndView modelAndView = new ModelAndView("/auditory/delete");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
}
