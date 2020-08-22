package com.maistruk.university.dao;

import java.util.List;

import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Teacher;

public interface CourseDao extends Dao<Course> {

    public Course getByName(String name);

    List<Course> getByGroup(Group group);

    public List<Course> getCoursesByTeacher(Teacher teacher);

}
