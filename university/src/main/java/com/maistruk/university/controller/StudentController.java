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

import com.maistruk.university.exceptions.StudentException;
import com.maistruk.university.mapper.StudentMapper;
import com.maistruk.university.model.Student;
import com.maistruk.university.model.dto.StudentDto;
import com.maistruk.university.service.StudentService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/student")
public class StudentController {

    StudentService studentService;
    StudentMapper studentMapper;

    public StudentController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @GetMapping(value = "/showAll")
    public ModelAndView showStudents() {
        ModelAndView modelAndView = new ModelAndView("/student/showAll");
        List<Student> students = studentService.getAll();
        modelAndView.addObject("students", students);
        return modelAndView;
    }

    @GetMapping(value = "/getById")
    public ModelAndView getById() {
        return new ModelAndView("/student/getById");
    }

    @RequestMapping("/getById")
    public ModelAndView showById(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("/student/showAll");
        Student student = studentService.getById(id);
        if (student == null) {
            return modelAndView.addObject("students", emptyList());
        }
        List<Student> students = asList(student);
        return modelAndView.addObject("students", students);
    }
    
    @GetMapping(value = "/create")
    public ModelAndView create() {
        return new ModelAndView("/student/create");
    }
    
    @RequestMapping("/create")
    public ModelAndView create(@ModelAttribute StudentDto studentDto) {
        try {
            Student student = studentMapper.mapStudentDtoToStudent(studentDto);
            studentService.create(student);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Student created");
        } catch (StudentException exception) {
            ModelAndView modelAndView = new ModelAndView("/student/create");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/update")
    public ModelAndView update() {
        return new ModelAndView("/student/update");
    }
    
    @RequestMapping("/update")
    public ModelAndView update(@ModelAttribute StudentDto studentDto) {
        try {
            Student student = studentMapper.mapStudentDtoToStudent(studentDto);
            studentService.update(student);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Student updated");
        } catch (StudentException exception) {
            ModelAndView modelAndView = new ModelAndView("/student/update");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/delete")
    public ModelAndView delete() {
        return new ModelAndView("/student/delete");
    }
    
    @RequestMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Integer id) {
        try {
            studentService.delete(id);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Student deleted");
        } catch (StudentException exception) {
            ModelAndView modelAndView = new ModelAndView("/student/delete");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }

}
