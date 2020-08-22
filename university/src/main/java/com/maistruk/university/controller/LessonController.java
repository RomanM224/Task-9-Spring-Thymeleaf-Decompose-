package com.maistruk.university.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.maistruk.university.exceptions.LessonException;
import com.maistruk.university.mapper.LessonMapper;
import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;
import com.maistruk.university.model.dto.LessonDto;
import com.maistruk.university.service.AuditoryService;
import com.maistruk.university.service.CourseService;
import com.maistruk.university.service.GroupService;
import com.maistruk.university.service.LessonService;
import com.maistruk.university.service.TeacherService;
import com.maistruk.university.service.TimeTableService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/lesson")
public class LessonController {

    GroupService groupService;
    CourseService courseService;
    TeacherService teacherService;
    AuditoryService auditoryService;
    TimeTableService timeTableService;
    LessonService lessonService;
    LessonMapper lessonMapper;

    public LessonController(GroupService groupService, CourseService courseService, TeacherService teacherService,
            AuditoryService auditoryService, TimeTableService timeTableService, LessonService lessonService, LessonMapper lessonMapper) {
        this.groupService = groupService;
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.auditoryService = auditoryService;
        this.timeTableService = timeTableService;
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping(value = "/showAll")
    public ModelAndView showLessons() {
        ModelAndView modelAndView = new ModelAndView("/lesson/showAll");
        List<Lesson> lessons = lessonService.getAll();
        modelAndView.addObject("lessons", lessons);
        return modelAndView;
    }

    @GetMapping(value = "/getById")
    public ModelAndView getById() {
        return new ModelAndView("/lesson/getById");
    }

    @RequestMapping("/getById")
    public ModelAndView showById(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("/lesson/showAll");
        Lesson lesson = lessonService.getById(id);
        if (lesson == null) {
            return modelAndView.addObject("lessons", emptyList());
        }
        List<Lesson> lessons = asList(lesson);
        return modelAndView.addObject("lessons", lessons);
    }

    @GetMapping(value = "/create")
    public ModelAndView create() {
        List<Group> groups = groupService.getAll();
        ModelAndView modelAndView = new ModelAndView("/lesson/create");
        modelAndView.addObject("groups", groups);
        List<Course> courses = courseService.getAll();
        modelAndView.addObject("courses", courses);
        List<Teacher> teachers = teacherService.getAll();
        modelAndView.addObject("teachers", teachers);
        List<Auditory> auditories = auditoryService.getAll();
        modelAndView.addObject("auditories", auditories);
        List<TimeTable> timeTables = timeTableService.getAll();
        return modelAndView.addObject("timeTables", timeTables);
    }

    @RequestMapping("/create")
    public ModelAndView create(@ModelAttribute LessonDto lessonDto) {
        try {
            Lesson lesson = lessonMapper.mapLessonDtoToLesson(lessonDto);
            lessonService.create(lesson);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Lesson created");
        } catch (LessonException exception) {
            List<Group> groups = groupService.getAll();
            ModelAndView modelAndView = new ModelAndView("/lesson/create");
            modelAndView.addObject("groups", groups);
            List<Course> courses = courseService.getAll();
            modelAndView.addObject("courses", courses);
            List<Teacher> teachers = teacherService.getAll();
            modelAndView.addObject("teachers", teachers);
            List<Auditory> auditories = auditoryService.getAll();
            modelAndView.addObject("auditories", auditories);
            List<TimeTable> timeTables = timeTableService.getAll();
            modelAndView.addObject("timeTables", timeTables);
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    

    @GetMapping(value = "/update")
    public ModelAndView update() {
        ModelAndView modelAndView = new ModelAndView("/lesson/update");
        List<Lesson> lessons = lessonService.getAll();
        modelAndView.addObject("lessons", lessons);
        List<Group> groups = groupService.getAll();
        modelAndView.addObject("groups", groups);
        List<Course> courses = courseService.getAll();
        modelAndView.addObject("courses", courses);
        List<Teacher> teachers = teacherService.getAll();
        modelAndView.addObject("teachers", teachers);
        List<Auditory> auditories = auditoryService.getAll();
        modelAndView.addObject("auditories", auditories);
        List<TimeTable> timeTables = timeTableService.getAll();
        return modelAndView.addObject("timeTables", timeTables);
    }

    @RequestMapping("/update")
    public ModelAndView update(@ModelAttribute LessonDto lessonDto) {
        try {
            Lesson lesson = lessonMapper.mapLessonDtoToLesson(lessonDto);
            lessonService.update(lesson);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Lesson updated");
        } catch (LessonException exception) {
            ModelAndView modelAndView = new ModelAndView("/lesson/update");
            List<Lesson> lessons = lessonService.getAll();
            modelAndView.addObject("lessons", lessons);
            List<Group> groups = groupService.getAll();
            modelAndView.addObject("groups", groups);
            List<Course> courses = courseService.getAll();
            modelAndView.addObject("courses", courses);
            List<Teacher> teachers = teacherService.getAll();
            modelAndView.addObject("teachers", teachers);
            List<Auditory> auditories = auditoryService.getAll();
            modelAndView.addObject("auditories", auditories);
            List<TimeTable> timeTables = timeTableService.getAll();
            modelAndView.addObject("timeTables", timeTables);
            return modelAndView.addObject("info", exception.getMessage());
        }
    }

    @GetMapping(value = "/delete")
    public ModelAndView delete() {
        return new ModelAndView("/lesson/delete");
    }

    @RequestMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Integer id) {
        try {
            lessonService.delete(id);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Lesson deleted");
        } catch (LessonException exception) {
            ModelAndView modelAndView = new ModelAndView("/lesson/delete");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }

    @GetMapping(value = "/showByGroup")
    public ModelAndView showByGroup() {
        ModelAndView modelAndView = new ModelAndView("/lesson/showByGroup");
        List<Group> groups = groupService.getAll();
        return modelAndView.addObject("groups", groups);
    }

    
    @RequestMapping("/showByGroup")
    public ModelAndView showByGroup(@RequestParam("id") Integer id, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("startDate") LocalDate startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("finishDate") LocalDate finishDate) {
        try {
            Group group = Group.builder().id(id).build();
            List<Lesson> lessons = lessonService.getByGroup(group, startDate, finishDate);
            ModelAndView modelAndView = new ModelAndView("/lesson/showAll");
            return modelAndView.addObject("lessons", lessons);
        } catch (LessonException exception) {
            ModelAndView modelAndView = new ModelAndView("/lesson/showByGroup");
            List<Group> groups = groupService.getAll();
            modelAndView.addObject("groups", groups);
            return modelAndView.addObject("info", exception.getMessage());
        }
    }

    @GetMapping(value = "/showByTeacher")
    public ModelAndView showByTeacher() {
        ModelAndView modelAndView = new ModelAndView("/lesson/showByTeacher");
        List<Teacher> teachers = teacherService.getAll();
        return modelAndView.addObject("teachers", teachers);
    }

    @RequestMapping("/showByTeacher")
    public ModelAndView showByTeacher(@RequestParam("id") Integer id, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("startDate") LocalDate startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("finishDate") LocalDate finishDate) {
        try {
            Teacher teacher = Teacher.builder().id(id).build();
            List<Lesson> lessons = lessonService.getByTeacher(teacher, startDate, finishDate);
            ModelAndView modelAndView = new ModelAndView("/lesson/showAll");
            return modelAndView.addObject("lessons", lessons);
        } catch (LessonException exception) {
            ModelAndView modelAndView = new ModelAndView("/lesson/showByTeacher");
            List<Teacher> teachers = teacherService.getAll();
            modelAndView.addObject("teachers", teachers);
            return modelAndView.addObject("info", exception.getMessage());
        }
    }

}
