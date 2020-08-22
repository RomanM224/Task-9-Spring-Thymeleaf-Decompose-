package com.maistruk.university.dao;

import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Student;

public interface GroupDao extends Dao<Group> {

    void deleteStudentFromGroup(Student student);

    void addStudentToGroup(Student student, Group group);

    void deleteCourseFromGroup(Course course, Group group);

    void addCourseToGroup(Course course, Group group);

    public Group getByName(String name);

}
