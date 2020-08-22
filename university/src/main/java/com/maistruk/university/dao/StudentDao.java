package com.maistruk.university.dao;

import java.util.List;

import com.maistruk.university.model.Group;
import com.maistruk.university.model.Student;

public interface StudentDao extends Dao<Student> {

    List<Student> getByGroup(Group group);

    Student getByFullName(String firstName, String lastName);

    Student getByStudentAndGroup(Student student, Group group);

}
