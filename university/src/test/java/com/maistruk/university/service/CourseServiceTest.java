package com.maistruk.university.service;

import static com.maistruk.university.service.CourseServiceTest.TestData.course;
import static com.maistruk.university.service.CourseServiceTest.TestData.course2;
import static com.maistruk.university.service.CourseServiceTest.TestData.course3;
import static com.maistruk.university.service.CourseServiceTest.TestData.courseId;
import static com.maistruk.university.service.CourseServiceTest.TestData.courseName;
import static com.maistruk.university.service.CourseServiceTest.TestData.courses;
import static com.maistruk.university.service.CourseServiceTest.TestData.notExistCourse;
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

import com.maistruk.university.dao.jdbc.JdbcCourseDao;
import com.maistruk.university.exceptions.CourseException;
import com.maistruk.university.model.Course;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private JdbcCourseDao courseDao;
    @InjectMocks
    private CourseService courseService;

    @Test
    public void givenCourse_whenCreate_thenCreated() throws CourseException {
        when(courseDao.getByName(courseName)).thenReturn(null);

        courseService.create(course);

        verify(courseDao).create(course);
    }

    @Test
    public void givenCourseWithExistName_whenCreate_thenNotCreated() {
        when(courseDao.getByName(courseName)).thenReturn(course);

        assertThrows(CourseException.class, () -> courseService.create(course));
    }

    @Test
    public void givenCourseWithSameNameAndId_whenUpdate_thenUpdated() throws CourseException {
        when(courseDao.getById(courseId)).thenReturn(course);
        when(courseDao.getByName(courseName)).thenReturn(course);

        courseService.update(course);

        verify(courseDao).update(course);
    }

    @Test
    public void givenCourseWithNewName_whenUpdate_thenUpdated() throws CourseException {
        when(courseDao.getById(courseId)).thenReturn(course);
        when(courseDao.getByName(courseName)).thenReturn(notExistCourse);

        courseService.update(course);

        verify(courseDao).update(course);
    }

    @Test
    public void givenCourseWithSameNameButDifferentId_whenNotUpdate_thenNotUpdated() {
        when(courseDao.getById(courseId)).thenReturn(course);
        when(courseDao.getByName(courseName)).thenReturn(course3);

        assertThrows(CourseException.class, () -> courseService.update(course));
    }

    @Test
    public void whenGetAll_thenGetAllCourses() {
        when(courseDao.getAll()).thenReturn(courses);

        List<Course> actualCourses = courseService.getAll();

        assertEquals(courses, actualCourses);
    }

    @Test
    public void givenCourseId_whenGetById_thenGetCourse() {
        when(courseDao.getById(courseId)).thenReturn(course2);

        Course actualCourse = courseService.getById(courseId);

        assertEquals(course2, actualCourse);
    }

    interface TestData {

        Integer courseId = 1;
        Integer courseId2 = 2;
        Integer courseId3 = 3;

        String courseName = "Database";
        String courseName2 = "Computer science";

        Course course = Course.builder().id(courseId).name(courseName).description(
                "Database is the study of an organized collection of data, generally stored and accessed electronically from a computer system.")
                .build();
        Course course2 = Course.builder().id(courseId2).name(courseName2).description(
                "Computer science is the study of processes that interact with data and that can be represented as data in the form of programs.")
                .build();
        Course course3 = Course.builder().id(courseId3).name(courseName).description(
                "Database is the study of an organized collection of data, generally stored and accessed electronically from a computer system.")
                .build();
        Course notExistCourse = null;
        List<Course> courses = asList(course, course2, course3);
    }

}
