package com.maistruk.university.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.maistruk.university.exceptions.GroupException;
import com.maistruk.university.model.Group;
import com.maistruk.university.service.GroupService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/group")
public class GroupController {

    GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(value = "/showAll")
    public ModelAndView showGroups() {
        ModelAndView modelAndView = new ModelAndView("/group/showAll");
        List<Group> groups = groupService.getAll();
        return modelAndView.addObject("groups", groups);
    }

    @GetMapping(value = "/getById")
    public ModelAndView getById() {
        return new ModelAndView("/group/getById");
    }

    @RequestMapping("/getById")
    public ModelAndView showById(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("/group/showAll");
        Group group = groupService.getById(id);
        if (group == null) {
            return modelAndView.addObject("groups", emptyList());
        }
        List<Group> groups = asList(group);
        return modelAndView.addObject("groups", groups);
    }
    
    @GetMapping(value = "/create")
    public ModelAndView create() {
        return new ModelAndView("/group/create");
    }
    
    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("name") String name) {
        try {
            Group group = Group.builder().name(name).build();
            groupService.create(group);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Group created");
        } catch (GroupException exception) {
            ModelAndView modelAndView = new ModelAndView("/group/create");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/update")
    public ModelAndView update() {
        return new ModelAndView("/group/update");
    }
    
    @RequestMapping("/update")
    public ModelAndView update(@RequestParam("id") Integer id, @RequestParam("name") String name) {
        try {
            Group group = Group.builder().id(id).name(name).build();
            groupService.update(group);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Group updated");
        } catch (GroupException exception) {
            ModelAndView modelAndView = new ModelAndView("/group/update");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/delete")
    public ModelAndView delete() {
        return new ModelAndView("/group/delete");
    }
    
    @RequestMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Integer id) {
        try {
            groupService.delete(id);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Group deleted");
        } catch (GroupException exception) {
            ModelAndView modelAndView = new ModelAndView("/group/delete");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
}
