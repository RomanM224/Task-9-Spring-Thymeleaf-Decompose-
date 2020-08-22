package com.maistruk.university.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maistruk.university.dao.CourseDao;
import com.maistruk.university.exceptions.CourseException;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Teacher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class CourseService {

    CourseDao courseDao;

    public void create(Course course) throws CourseException {
        log.info(String.format("Create course (%s) [Service layer]", course.toStringIdName()));
        if (courseDao.getByName(course.getName()) != null) {
            throw new CourseException("Course with this name already exist");
        }
        courseDao.create(course);
    }

    public void update(Course course) throws CourseException {
        if (courseDao.getById(course.getId()) == null) {
            log.info("Update course [Service layer]");
            throw new CourseException("Course does not exist");
        }
        log.info(String.format("Update course (%s) [Service layer]", course.toStringIdName()));
        Course courseGetByName = courseDao.getByName(course.getName());
        if ((courseGetByName != null) && (!courseGetByName.getId().equals(course.getId()))) {
            throw new CourseException("Course name and course id does not match");
        }
        courseDao.update(course);
    }

    public void delete(Integer id) throws CourseException {
        log.info(String.format("Delete course by id=%d [Service layer]", id));
        if (courseDao.getById(id) == null) {
            throw new CourseException("Course does not exist");
        }
        courseDao.delete(id);
    }

    public List<Course> getAll() {
        log.info("Get all courses [Service layer]");
        return courseDao.getAll();
    }

    public Course getById(Integer id) {
        log.info(String.format("Get course by id=%d [Service layer]", id));
        return courseDao.getById(id);
    }

    public List<Course> getByGroup(Group group) {
        if (group != null) {
            log.info(String.format("Get courses by group (%s) [Service layer]", group.toString()));
            return courseDao.getByGroup(group);
        }
        log.info("Get courses by group (group does not exist) [Service layer]");
        return new ArrayList<>();
    }

    public List<Course> getCoursesByTeacher(Teacher teacher) {
        if (teacher != null) {
            log.info(String.format("Get courses by teacher (%s) [Service layer]", teacher.toString()));
            return courseDao.getCoursesByTeacher(teacher);
        }
        log.info("Get courses by teacher (teacher does not exist) [Service layer]");
        return new ArrayList<>();
    }

}
