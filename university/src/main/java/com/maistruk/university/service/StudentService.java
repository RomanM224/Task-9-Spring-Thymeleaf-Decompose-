package com.maistruk.university.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maistruk.university.dao.StudentDao;
import com.maistruk.university.exceptions.StudentException;
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
public class StudentService {

    StudentDao studentDao;

    public void create(Student student) throws StudentException {
        log.info(String.format("Create student (%s) [Service layer]", student.toString()));
        if (studentDao.getByFullName(student.getFirstName(), student.getLastName()) != null) {
            throw new StudentException("Student with this name already exist");
        }
        studentDao.create(student);

    }

    public void update(Student student) throws StudentException {
        if (studentDao.getById(student.getId()) == null) {
            log.info("Update student [Service layer]");
            throw new StudentException("Student does not exist");
        }
        if (studentDao.getByFullName(student.getFirstName(), student.getLastName()) != null) {
            log.info("Update student [Service layer]");
            throw new StudentException("Student with this name already exist");
        }
        log.info(String.format("Update student (%s) [Service layer]", student.toString()));
        studentDao.update(student);
    }

    public void delete(Integer id) throws StudentException {
        log.info(String.format("Delete student by id=%d [Service layer]", id));
        if (studentDao.getById(id) == null) {
            throw new StudentException("Student does not exist");
        }
        studentDao.delete(id);
    }

    public List<Student> getAll() {
        log.info("Get all students [Service layer]");
        return studentDao.getAll();
    }

    public Student getById(Integer id) {
        log.info(String.format("Get student by id=%d [Service layer]", id));
        return studentDao.getById(id);
    }

    public List<Student> getByGroup(Group group) {
        if (group != null) {
            log.info(String.format("Get students by group (%s) [Service layer]", group.toString()));
            return studentDao.getByGroup(group);
        }
        log.info("Get students by group (group does not exist) [Service layer]");
        return new ArrayList<>();
    }

    public Student getByStudentAndGroup(Student student, Group group) {
        log.info(String.format("Check if student (%s) is in group (%s) [Service layer]", student.toString(),
                group.toString()));
        return studentDao.getByStudentAndGroup(student, group);
    }
}
