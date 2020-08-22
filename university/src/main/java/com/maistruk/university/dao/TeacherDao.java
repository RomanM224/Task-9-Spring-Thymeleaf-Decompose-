package com.maistruk.university.dao;

import java.util.List;

import com.maistruk.university.model.Course;
import com.maistruk.university.model.Teacher;

public interface TeacherDao extends Dao<Teacher> {

    List<Teacher> getByCourse(Course course);

    void deleteCourseFromTeacher(Course course, Teacher teacher);

    void addCourseToGroup(Course course, Teacher teacher);

    public Teacher getByFullName(String firstName, String lastName);

}
