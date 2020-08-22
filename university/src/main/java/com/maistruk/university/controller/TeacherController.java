package com.maistruk.university.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.maistruk.university.exceptions.TeacherException;
import com.maistruk.university.mapper.TeacherMapper;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.dto.TeacherDto;
import com.maistruk.university.service.TeacherService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {

    TeacherService teacherService;
    TeacherMapper teacherMapper;

    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping(value = "/showAll")
    public ModelAndView showTeachers() {
        ModelAndView modelAndView = new ModelAndView("/teacher/showAll");
        List<Teacher> teachers = teacherService.getAll();
        return modelAndView.addObject("teachers", teachers);
    }

    @GetMapping(value = "/getById")
    public ModelAndView getById() {
        return new ModelAndView("/teacher/getById");
    }

    @RequestMapping("/getById")
    public ModelAndView showById(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("/teacher/showAll");
        Teacher teacher = teacherService.getById(id);
        if (teacher == null) {
            return modelAndView.addObject("teachers", emptyList());
        }
        List<Teacher> teachers = asList(teacher);
        return modelAndView.addObject("teachers", teachers);
    }
    
    @GetMapping(value = "/create")
    public ModelAndView create() {
        return new ModelAndView("/teacher/create");
    }
    
    @RequestMapping("/create")
    public ModelAndView create(@ModelAttribute TeacherDto teacherDto) {
        try {
            Teacher teacher = teacherMapper.mapTeacherDtoToTeacher(teacherDto);
            teacherService.create(teacher);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Teacher created");
        } catch (TeacherException exception) {
            ModelAndView modelAndView = new ModelAndView("/teacher/create");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/update")
    public ModelAndView update() {
        return new ModelAndView("/teacher/update");
    }
    
    @RequestMapping("/update")
    public ModelAndView update(@ModelAttribute TeacherDto teacherDto) {
        try {
            Teacher teacher = teacherMapper.mapTeacherDtoToTeacher(teacherDto);
            teacherService.update(teacher);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Teacher updated");
        } catch (TeacherException exception) {
            ModelAndView modelAndView = new ModelAndView("/teacher/update");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/delete")
    public ModelAndView delete() {
        return new ModelAndView("/teacher/delete");
    }
    
    @RequestMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Integer id) {
        try {
            teacherService.delete(id);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Teacher deleted");
        } catch (TeacherException exception) {
            ModelAndView modelAndView = new ModelAndView("/teacher/delete");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
}
