package com.maistruk.university.service;

import static com.maistruk.university.service.TeacherServiceTest.TestData.course;
import static com.maistruk.university.service.TeacherServiceTest.TestData.course2;
import static com.maistruk.university.service.TeacherServiceTest.TestData.firstName;
import static com.maistruk.university.service.TeacherServiceTest.TestData.lastName;
import static com.maistruk.university.service.TeacherServiceTest.TestData.notExisTeacher;
import static com.maistruk.university.service.TeacherServiceTest.TestData.notExistCourse;
import static com.maistruk.university.service.TeacherServiceTest.TestData.teacher;
import static com.maistruk.university.service.TeacherServiceTest.TestData.teacher2;
import static com.maistruk.university.service.TeacherServiceTest.TestData.teacherCourses;
import static com.maistruk.university.service.TeacherServiceTest.TestData.teacherId;
import static com.maistruk.university.service.TeacherServiceTest.TestData.teacherId2;
import static com.maistruk.university.service.TeacherServiceTest.TestData.teachers;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maistruk.university.dao.jdbc.JdbcTeacherDao;
import com.maistruk.university.exceptions.TeacherException;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private JdbcTeacherDao teacherDao;
    @Mock
    private CourseService courseService;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void givenTeacher_whenCreate_thenCreated() throws TeacherException {
        when(teacherDao.getByFullName(firstName, lastName)).thenReturn(notExisTeacher);

        teacherService.create(teacher);

        verify(teacherDao).create(teacher);
    }

    @Test
    public void givenTeacherWithExistName_whenCreate_thenNotCreated() {
        when(teacherDao.getByFullName(firstName, lastName)).thenReturn(teacher);

        assertThrows(TeacherException.class, () -> teacherService.create(teacher));
    }

    @Test
    public void givenTeacher_whenUpdate_thenUpdated() throws TeacherException {
        when(teacherDao.getById(teacherId)).thenReturn(teacher);
        when(teacherDao.getByFullName(firstName, lastName)).thenReturn(notExisTeacher);

        teacherService.update(teacher);

        verify(teacherDao).update(teacher);
    }

    @Test
    public void givenNotExistTeacher_whenUpdate_thenNotUpdated() {
        when(teacherDao.getById(teacherId)).thenReturn(notExisTeacher);

        assertThrows(TeacherException.class, () -> teacherService.update(teacher));
    }

    @Test
    public void givenTeacherWithExistName_whenUpdate_thenNotUpdated() {
        when(teacherDao.getById(teacherId)).thenReturn(teacher);
        when(teacherDao.getByFullName(firstName, lastName)).thenReturn(teacher);

        assertThrows(TeacherException.class, () -> teacherService.update(teacher));
    }

    @Test
    public void whenGetAll_thenGetAllTeachers() {
        when(teacherDao.getAll()).thenReturn(teachers);

        List<Teacher> actualTeachers = teacherService.getAll();

        assertEquals(teachers, actualTeachers);
    }

    @Test
    public void givenTeacherId_whenGetById_thenGetTeacher() {
        when(teacherDao.getById(teacherId2)).thenReturn(teacher2);

        Teacher actualTeacher = teacherService.getById(teacherId2);

        assertEquals(teacher2, actualTeacher);
    }

    @Test
    public void givenTeacher_whenGetCoursesByTeaher_thenReturnCourses() {
        when(courseService.getCoursesByTeacher(teacher2)).thenReturn(teacherCourses);

        List<Course> actualCourses = teacherService.getCoursesByTeaher(teacher2);

        assertEquals(teacherCourses, actualCourses);
    }

    @Test
    public void givenCourseAndTeacher_whenDeleteCourseFromTeacher_thenDeleted() throws TeacherException {
        when(courseService.getCoursesByTeacher(teacher)).thenReturn(teacherCourses);

        teacherService.deleteCourseFromTeacher(course, teacher);

        verify(teacherDao).deleteCourseFromTeacher(course, teacher);
    }

    @Test
    public void givenCourseAndNotExistTeacher_whenDeleteCourseFromTeacher_thenNotDeleted() {

        assertThrows(TeacherException.class, () -> teacherService.deleteCourseFromTeacher(course, notExisTeacher));
    }

    @Test
    public void givenNotExistCourseAndTeacher_whenDeleteCourseFromTeacher_thenNotDeleted() {

        assertThrows(TeacherException.class, () -> teacherService.deleteCourseFromTeacher(notExistCourse, teacher));
    }

    @Test
    public void givenCourseWhichTeacherDoesNotContain_whenDeleteCourseFromTeacher_thenNotDeleted() {
        when(courseService.getCoursesByTeacher(teacher)).thenReturn(teacherCourses);

        assertThrows(TeacherException.class, () -> teacherService.deleteCourseFromTeacher(course2, teacher));
    }

    @Test
    public void givenCourseAndTeacher_whenAddCourseToTeacher_thenAdded() throws TeacherException {
        when(courseService.getCoursesByTeacher(teacher)).thenReturn(teacherCourses);

        teacherService.addCourseToTeacher(course2, teacher);

        verify(teacherDao).addCourseToGroup(course2, teacher);
    }

    @Test
    public void givenCourseAndNotExistTeacher_whenAddCourseToTeacher_thenNoAdded() {

        assertThrows(TeacherException.class, () -> teacherService.addCourseToTeacher(course2, notExisTeacher));
    }

    @Test
    public void givenNotExistCourseAndTeacher_whenAddCourseToTeacher_thenNoAdded() {

        assertThrows(TeacherException.class, () -> teacherService.addCourseToTeacher(notExistCourse, teacher));
    }

    @Test
    public void givenCourseWhichTeacherContain_whenAddCourseToTeacher_thenNoAdded() {
        when(courseService.getCoursesByTeacher(teacher)).thenReturn(teacherCourses);

        assertThrows(TeacherException.class, () -> teacherService.addCourseToTeacher(course, teacher));
    }

    interface TestData {

        Integer teacherId = 1;
        Integer teacherId2 = 2;
        Integer teacherId3 = 3;
        Integer courseId = 2;
        Integer courseId2 = 3;
        String firstName = "Zoe";
        String lastName = "Barnes";

        Teacher teacher = Teacher.builder().id(teacherId).firstName(firstName).lastName(lastName).build();
        Teacher teacher2 = Teacher.builder().id(teacherId2).firstName("Peter").lastName("Russo").build();
        Teacher teacher3 = Teacher.builder().id(teacherId3).firstName("Lucas").lastName("Goodwin").build();
        Teacher notExisTeacher = null;

        List<Teacher> teachers = asList(teacher, teacher2, teacher3);

        Course course = Course.builder().id(courseId).name("Computer science").description(
                "Computer science is the study of processes that interact with data and that can be represented as data in the form of programs.")
                .build();
        Course course2 = Course.builder().id(courseId2).name("Java").description("Java programming language").build();
        Course notExistCourse = null;
        List<Course> teacherCourses = asList(course);

    }

}
