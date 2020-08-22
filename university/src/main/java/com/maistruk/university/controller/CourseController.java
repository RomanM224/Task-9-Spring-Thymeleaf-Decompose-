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

import com.maistruk.university.exceptions.CourseException;
import com.maistruk.university.mapper.CourseMapper;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.dto.CourseDto;
import com.maistruk.university.service.CourseService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/course")
public class CourseController {

    CourseService courseService;
    CourseMapper courseMapper;

    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @GetMapping(value = "/showAll")
    public ModelAndView showCourses() {
        ModelAndView modelAndView = new ModelAndView("/course/showAll");
        List<Course> courses = courseService.getAll();
        return modelAndView.addObject("courses", courses);
    }

    @GetMapping(value = "/getById")
    public ModelAndView getById() {
        return new ModelAndView("/course/getById");
    }

    @RequestMapping("/getById")
    public ModelAndView showById(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("/course/showAll");
        Course course = courseService.getById(id);
        if (course == null) {
            return modelAndView.addObject("courses", emptyList());
        }
        List<Course> courses = asList(course);
        return modelAndView.addObject("courses", courses);
    }
    
    @GetMapping(value = "/create")
    public ModelAndView create() {
        return new ModelAndView("/course/create");
    }
    
    @RequestMapping("/create")
    public ModelAndView create(@ModelAttribute CourseDto courseDto) {
        try {
            Course course = courseMapper.mapCourseDtoToCourse(courseDto);
            courseService.create(course);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Course created");
        } catch (CourseException exception) {
            ModelAndView modelAndView = new ModelAndView("/course/create");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/update")
    public ModelAndView update() {
        return new ModelAndView("/course/update");
    }
    
    @RequestMapping("/update")
    public ModelAndView update(@ModelAttribute CourseDto courseDto) {
        try {
            Course course = courseMapper.mapCourseDtoToCourse(courseDto);
            courseService.update(course);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Course updated");
        } catch (CourseException exception) {
            ModelAndView modelAndView = new ModelAndView("/course/update");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/delete")
    public ModelAndView delete() {
        return new ModelAndView("/course/delete");
    }
    
    @RequestMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Integer id) {
        try {
            courseService.delete(id);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Course deleted");
        } catch (CourseException exception) {
            ModelAndView modelAndView = new ModelAndView("/course/delete");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
}
