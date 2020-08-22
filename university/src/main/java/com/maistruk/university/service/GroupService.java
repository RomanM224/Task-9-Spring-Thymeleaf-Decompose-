package com.maistruk.university.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maistruk.university.dao.GroupDao;
import com.maistruk.university.exceptions.GroupException;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Student;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class GroupService {

    GroupDao groupDao;
    CourseService courseService;
    StudentService studentService;

    public void create(Group group) throws GroupException {
        log.info(String.format("Create group (%s) [Service layer]", group.toString()));
        if (groupDao.getByName(group.getName()) != null) {
            throw new GroupException("Group with this name already exist");
        }
        groupDao.create(group);
    }

    public void update(Group group) throws GroupException {
        if (groupDao.getById(group.getId()) == null) {
            log.info("Update group [Service layer]");
            throw new GroupException("Group does not exist");
        }
        log.info(String.format("Update group (%s) [Service layer]", group.toString()));
        if (groupDao.getByName(group.getName()) != null) {
            throw new GroupException("Group with this name already exist");
        }
        groupDao.update(group);
    }

    public void delete(Integer id) throws GroupException {
        log.info(String.format("Delete group by id=%d [Service layer]", id));
        if (groupDao.getById(id) == null) {
            throw new GroupException("Group does not exist");
        }
        groupDao.delete(id);
    }

    public List<Group> getAll() {
        log.info("Get all groups [Service layer]");
        return groupDao.getAll();
    }

    public Group getById(Integer id) {
        log.info(String.format("Get group by id=%d [Service layer]", id));
        return groupDao.getById(id);
    }

    public List<Student> getStudentsByGroup(Group group) {
        if (group != null) {
            log.info(String.format("Get students by group (%s) [Service layer]", group.toString()));
            return studentService.getByGroup(group);
        }
        log.info("Get students by group (group does not exist) [Service layer]");
        return new ArrayList<>();
    }

    public List<Course> getCoursesByGroup(Group group) {
        if (group != null) {
            log.info(String.format("Get courses by group (%s) [Service layer]", group.toString()));
            return courseService.getByGroup(group);
        }
        log.info("Get courses by group (group not exist) [Service layer]");
        return new ArrayList<>();
    }

    public void deleteStudentFromGroup(Student student) throws GroupException {
        if (student == null) {
            log.info("Delete student from any group [Service layer]");
            throw new GroupException("Student does not exist");
        }
        log.info(String.format("Delete student (%s) from any group [Service layer]", student.toString()));
        groupDao.deleteStudentFromGroup(student);
    }

    public void addStudentToGroup(Student student, Group group) throws GroupException {
        if (group == null) {
            log.info("Add student to group [Service layer]");
            throw new GroupException("Group does not exist");
        }
        if (student == null) {
            log.info("Add student to group [Service layer]");
            throw new GroupException("Student does not exist");
        }
        if (studentService.getByStudentAndGroup(student, group) != null) {
            log.info(String.format("Add student (%s) to group (%s) [Service layer]", student.toString(),
                    group.toString()));
            throw new GroupException("Student already in this group");
        }
        log.info(String.format("Add student (%s) to group (%s) [Service layer]", student.toString(), group.toString()));
        groupDao.addStudentToGroup(student, group);
    }

    public void deleteCourseFromGroup(Course course, Group group) throws GroupException {
        if (group == null) {
            log.info("Delete course from group [Service layer]");
            throw new GroupException("Group does not exist");
        }
        if (course == null) {
            log.info("Delete course from group [Service layer]");
            throw new GroupException("Course does not exist");
        }
        if (!courseService.getByGroup(group).stream().filter(myCourse -> myCourse.getId().equals(course.getId()))
                .findFirst().isPresent()) {
            log.info(String.format("Delete course (%s) from group (%s) [Service layer]", course.toStringIdName(),
                    group.toString()));
            throw new GroupException("Course does not exist in this group");
        }
        log.info(String.format("Delete course (%s) from group (%s) [Service layer]", course.toStringIdName(),
                group.toString()));
        groupDao.deleteCourseFromGroup(course, group);
    }

    public void addCourseToGroup(Course course, Group group) throws GroupException {
        if (group == null) {
            log.info("Add course to group [Service layer]");
            throw new GroupException("Group does not exist");
        }
        if (course == null) {
            log.info("Add course to group [Service layer]");
            throw new GroupException("Course does not exist");
        }
        if (courseService.getByGroup(group).stream().filter(myCourse -> myCourse.getId().equals(course.getId()))
                .findFirst().isPresent()) {
            log.info(String.format("Add course (%s) to group (%s) [Service layer]", course.toStringIdName(),
                    group.toString()));
            throw new GroupException("This course already in this group");
        }
        log.info(String.format("Add course (%s) to group (%s) [Service layer]", course.toStringIdName(),
                group.toString()));
        groupDao.addCourseToGroup(course, group);
    }

}
