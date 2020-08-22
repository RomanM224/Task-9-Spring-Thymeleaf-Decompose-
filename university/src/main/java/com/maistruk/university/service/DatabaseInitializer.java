package com.maistruk.university.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import com.maistruk.university.dao.AuditoryDao;
import com.maistruk.university.dao.CourseDao;
import com.maistruk.university.dao.GroupDao;
import com.maistruk.university.dao.LessonDao;
import com.maistruk.university.dao.StudentDao;
import com.maistruk.university.dao.TeacherDao;
import com.maistruk.university.dao.TimeTableDao;
import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Student;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class DatabaseInitializer {

    StudentDao studentDao;
    GroupDao groupDao;
    CourseDao courseDao;
    AuditoryDao auditoryDao;
    TeacherDao teacherDao;
    TimeTableDao timeTableDao;
    LessonDao lessonDao;
    InfoGenerator infoGenerator;
    DataSource dataSource;

    public void init() {
        importSchema(dataSource);
        loadData();
    }

    private void importSchema(DataSource dataSource) {
        Resource resource = new ClassPathResource("schema.sql");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
        databasePopulator.execute(dataSource);
    }

    private void loadData() {
        List<Course> courses = generateCourses();
        List<Group> groups = generateGroups(courses);
        List<Student> students = generateStudents(groups);
        setStudentsToGroup(students, groups);
        setCoursesToGroup(groups);
        List<Teacher> teachers = generateTeachers(courses);
        setCoursesToTeacher(teachers);
        List<Auditory> auditories = generateAuditories();
        List<TimeTable> timeTables = generateTimeTables();
        List<Lesson> lessons = generateLessons(groups, teachers, timeTables, auditories);
    }

    public List<Course> generateCourses() {
        List<Course> courses = infoGenerator.generateCourses();
        courses.forEach(courseDao::create);
        return courses;
    }

    public List<Group> generateGroups(List<Course> courses) {
        List<Group> groups = infoGenerator.generateGroups(courses);
        groups.forEach(groupDao::create);
        return groups;
    }

    private List<Student> generateStudents(List<Group> groups) {
        List<Student> students = infoGenerator.generateStudents(groups);
        students.forEach(studentDao::create);
        return students;
    }

    private void setStudentsToGroup(List<Student> students, List<Group> groups) {
        infoGenerator.setStudentsToGroup(students, groups);
        for (Group group : groups) {
            for (Student student : group.getStudents()) {
                groupDao.addStudentToGroup(student, group);
            }
        }
    }

    private void setCoursesToGroup(List<Group> groups) {
        for (Group group : groups) {
            for (Course course : group.getCourses()) {
                groupDao.addCourseToGroup(course, group);
            }
        }
    }

    public List<Teacher> generateTeachers(List<Course> courses) {
        List<Teacher> teachers = infoGenerator.generateTeachers(courses);
        teachers.forEach(teacherDao::create);
        return teachers;
    }

    public void setCoursesToTeacher(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            for (Course course : teacher.getCourses()) {
                teacherDao.addCourseToGroup(course, teacher);
            }
        }
    }

    public List<Auditory> generateAuditories() {
        List<Auditory> auditories = infoGenerator.generateAuditories();
        auditories.forEach(auditoryDao::create);
        return auditories;
    }

    public List<TimeTable> generateTimeTables() {
        List<TimeTable> timeTables = infoGenerator.generateTimeTables();
        timeTables.forEach(timeTableDao::create);
        return timeTables;
    }

    public List<Lesson> generateLessons(List<Group> groups, List<Teacher> teachers, List<TimeTable> timeTables,
            List<Auditory> auditories) {
        List<Lesson> lessons = infoGenerator.generateLessons(groups, teachers, timeTables, auditories);
        lessons.forEach(lessonDao::create);
        return lessons;
    }
}
