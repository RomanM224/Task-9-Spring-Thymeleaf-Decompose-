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
import com.maistruk.university.model.Teacher;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcTeacherDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTeacherDao teacherDao;

    @Test
    public void givenNewTeacher_whenCreate_thenCreatedAndGetGeneratedId() {
        Teacher expectedTeacher = Teacher.builder().id(4).firstName("name").lastName("surname").build();
        Teacher actualTeacher = Teacher.builder().firstName("name").lastName("surname").build();

        teacherDao.create(actualTeacher);

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    public void givenExistingTeacher_whenUpdate_thenUpdated() {
        Teacher expectedTeacher = Teacher.builder().id(2).firstName("Name").lastName("Surname").build();

        teacherDao.update(expectedTeacher);

        Integer rowsInDatabaseExpected = 1;
        Integer rowsInDatabaseActual = countRowsInTableWhere(jdbcTemplate, "teachers",
                "first_name = 'Name' and last_name = 'Surname' and id = 2");
        assertEquals(rowsInDatabaseExpected, rowsInDatabaseActual);
    }

    @Test
    public void whenGetAll_thenGetAllTeachers() {
        Teacher teacher1 = Teacher.builder().id(1).firstName("Zoe").lastName("Barnes").build();
        Teacher teacher2 = Teacher.builder().id(2).firstName("Peter").lastName("Russo").build();
        Teacher teacher3 = Teacher.builder().id(3).firstName("Lucas").lastName("Goodwin").build();
        List<Teacher> expectedTeachers = asList(teacher1, teacher2, teacher3);

        List<Teacher> actualTeachers = teacherDao.getAll();

        assertEquals(expectedTeachers, actualTeachers);
    }

    @Test
    public void givenTeacherId_whenGetById_thenGetTeacher() {
        Integer id = 2;
        Teacher expectedTeacher = Teacher.builder().id(id).firstName("Peter").lastName("Russo").build();

        Teacher actualTeacher = teacherDao.getById(id);

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    public void givenTeacherId_whenDelete_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "teachers");

        teacherDao.delete(2);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "teachers");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenCourseId_whenGetByCourseId_thenGetTeacher() {
        Teacher expectedTeacher = Teacher.builder().id(2).firstName("Peter").lastName("Russo").build();
        Course course = Course.builder().id(2).build();

        Teacher actualTeacher = teacherDao.getByCourse(course).get(0);

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    public void givenTeacherAndCourse_whenDeleteCourseFromTeacher_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "courses_teacher WHERE teacher_id = 2");
        Teacher teacher = Teacher.builder().id(2).build();
        Course course = Course.builder().id(2).build();

        teacherDao.deleteCourseFromTeacher(course, teacher);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "courses_teacher WHERE teacher_id = 2");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenTeacherAndCourse_whenAddCoursetoTeacher_thenAdded() {
        Integer rowsBeforeAdd = countRowsInTable(jdbcTemplate, "courses_teacher WHERE teacher_id = 2");
        Teacher teacher = Teacher.builder().id(2).build();
        Course course = Course.builder().id(1).build();

        teacherDao.addCourseToGroup(course, teacher);

        Integer rowsAfterAdd = countRowsInTable(jdbcTemplate, "courses_teacher WHERE teacher_id = 2");
        assertEquals(rowsBeforeAdd + 1, rowsAfterAdd);
    }

}
