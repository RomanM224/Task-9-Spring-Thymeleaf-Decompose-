package com.maistruk.university.dao.jdbc;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.maistruk.university.config.TestConfig;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcCourseDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcCourseDao courseDao;

    @Test
    public void givenNewCourse_whenCreate_thenCreatedAndGetGeneratedId() {
        Course expectedCourse = Course.builder().id(4).name("Course Name").description("Course Description").build();
        Course actualCourse = Course.builder().name("Course Name").description("Course Description").build();

        courseDao.create(actualCourse);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    public void givenExistingCourse_whenUpdate_thenUpdated() {
        Course expectedCourse = Course.builder().id(1).name("Math").description("Course Discription").build();

        courseDao.update(expectedCourse);

        Integer rowsInDatabaseExpected = 1;
        Integer rowsInDatabaseActual = countRowsInTableWhere(jdbcTemplate, "courses",
                "name = 'Math' and description = 'Course Discription' and id = 1");
        assertEquals(rowsInDatabaseExpected, rowsInDatabaseActual);
    }

    @Test
    public void whenGetAll_thenGetAllCourses() {
        Course course1 = Course.builder().id(1).name("Database").description("Database description").build();
        Course course2 = Course.builder().id(2).name("Computer science").description("Computer science description").build();
        Course course3 = Course.builder().id(3).name("Geography").description("Geography description").build();
        List<Course> expectedCourses = asList(course1, course2, course3);

        List<Course> actualCourses = courseDao.getAll();

        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    public void givenCourseId_whenGetById_thenGetCourse() {
        Integer id = 2;
        Course expectedCourse = Course.builder().id(id).name("Computer science").description("Computer science description").build();

        Course actualCourse = courseDao.getById(id);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    public void givenCourseId_whenDelete_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "courses");

        courseDao.delete(2);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "courses");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenGroupId_whenGetByGroupId_thenGetCourses() {
        Group group = Group.builder().id(2).build();
        Course course1 = Course.builder().id(2).name("Computer science").description("Computer science description").build();
        Course course2 = Course.builder().id(3).name("Geography").description("Geography description").build();
        List<Course> expectedCourses = asList(course1, course2);

        List<Course> actualCourses = courseDao.getByGroup(group);

        assertEquals(expectedCourses, actualCourses);
    }

}
