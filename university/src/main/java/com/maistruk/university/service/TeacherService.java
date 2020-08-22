package com.maistruk.university.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maistruk.university.dao.TeacherDao;
import com.maistruk.university.exceptions.TeacherException;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Teacher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TeacherService {

    TeacherDao teacherDao;
    CourseService courseService;

    public void create(Teacher teacher) throws TeacherException {
        log.info(String.format("Create teacher (%s) [Service layer]", teacher.toString()));
        if (teacherDao.getByFullName(teacher.getFirstName(), teacher.getLastName()) != null) {
            throw new TeacherException("Teacher wiht this name already exist");
        }
        teacherDao.create(teacher);
    }

    public void update(Teacher teacher) throws TeacherException {
        if (teacherDao.getById(teacher.getId()) == null) {
            log.info("Update teacher [Service layer]");
            throw new TeacherException("Teacher does not exist");
        }
        if (teacherDao.getByFullName(teacher.getFirstName(), teacher.getLastName()) != null) {
            log.info("Update teacher [Service layer]");
            throw new TeacherException("Teacher wiht this name already exist");
        }
        log.info(String.format("Update teacher (%s) [Service layer]", teacher.toString()));
        teacherDao.update(teacher);
    }

    public void delete(Integer id) throws TeacherException {
        log.info(String.format("Delete teacher by id=%d [Service layer]", id));
        if (teacherDao.getById(id) == null) {
            throw new TeacherException("Teacher does not exist");
        }
        teacherDao.delete(id);
    }

    public List<Teacher> getAll() {
        log.info("Get all teachers [Service layer]");
        return teacherDao.getAll();
    }

    public Teacher getById(Integer id) {
        log.info(String.format("Get teacher by id=%d [Service layer]", id));
        return teacherDao.getById(id);
    }

    public List<Teacher> getByCourse(Course course) {
        if (course == null) {
            log.info("Get teachers by course (teacher not exist) [Service layer]");
            return new ArrayList<>();
        }
        log.info(String.format("Get teachers by course (%s) [Service layer]", course.toStringIdName()));
        return teacherDao.getByCourse(course);
    }

    public List<Course> getCoursesByTeaher(Teacher teacher) {
        if (teacher == null) {
            log.info("Get courses by teacher (teacher not exist) [Service layer]");
            return new ArrayList<>();
        }
        log.info(String.format("Get courses by teacher (%s) [Service layer]", teacher.toString()));
        return courseService.getCoursesByTeacher(teacher);
    }

    public void deleteCourseFromTeacher(Course course, Teacher teacher) throws TeacherException {
        if (teacher == null) {
            log.info("Delete course from teacher [Service layer]");
            throw new TeacherException("Teacher not exist");
        }
        if (course == null) {
            log.info("Delete course from teacher [Service layer]");
            throw new TeacherException("Course not exist");
        }
        if (!courseService.getCoursesByTeacher(teacher).stream()
                .filter(myCourse -> myCourse.getId().equals(course.getId())).findFirst().isPresent()) {
            log.info("Delete course from teacher [Service layer]");
            throw new TeacherException("Teacher does not contain this course");
        }
        log.info(String.format("Delete course (%s) from teacher (%s) [Service layer]", course.toStringIdName(),
                teacher.toString()));
        teacherDao.deleteCourseFromTeacher(course, teacher);
    }

    public void addCourseToTeacher(Course course, Teacher teacher) throws TeacherException {
        if (teacher == null) {
            log.info("Add course to teacher [Service layer]");
            throw new TeacherException("Teacher not exist");
        }
        if (course == null) {
            log.info("Add course to teacher [Service layer]");
            throw new TeacherException("Course not exist");
        }
        if (courseService.getCoursesByTeacher(teacher).stream()
                .filter(myCourse -> myCourse.getId().equals(course.getId())).findFirst().isPresent()) {
            log.info("Add course to teacher [Service layer]");
            throw new TeacherException("Teacher already have this course");
        }
        log.info(String.format("Add course (%s) to teacher (%s) [Service layer]", course.toStringIdName(),
                teacher.toString()));
        teacherDao.addCourseToGroup(course, teacher);
    }

}
