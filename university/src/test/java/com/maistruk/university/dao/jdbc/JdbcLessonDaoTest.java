package com.maistruk.university.dao.jdbc;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
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
import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcLessonDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcLessonDao lessonDao;

    @Test
    public void givenNewLesson_whenCreate_thenCreatedAndGetGeneratedId() {
        Course course = Course.builder().id(1).build();
        Group group = Group.builder().id(3).build();
        Teacher teacher = Teacher.builder().id(1).build();
        TimeTable timeTable = TimeTable.builder().id(2).build();
        Auditory auditory = Auditory.builder().id(1).number(100).build();
        LocalDate date = LocalDate.parse("2020-05-05");
        Lesson expectedLesson = Lesson.builder().id(7).course(course).group(group).teacher(teacher).timeTable(timeTable)
                .auditory(auditory).date(date).build();
        Lesson actualLesson = Lesson.builder().course(course).group(group).teacher(teacher).timeTable(timeTable)
                .auditory(auditory).date(date).build();

        lessonDao.create(actualLesson);

        assertEquals(expectedLesson, actualLesson);
    }

    @Test
    public void givenExistingLesson_whenUpdate_thenUpdated() {
        Course course = Course.builder().id(1).build();
        Group group = Group.builder().id(2).build();
        Teacher teacher = Teacher.builder().id(3).build();
        TimeTable timeTable = TimeTable.builder().id(2).build();
        Auditory auditory = Auditory.builder().id(3).number(100).build();
        LocalDate date = LocalDate.parse("2020-05-05");
        Lesson expectedLesson = Lesson.builder().id(2).course(course).group(group).teacher(teacher).timeTable(timeTable)
                .auditory(auditory).date(date).build();

        lessonDao.update(expectedLesson);

        Integer rowsInDatabaseExpected = 1;
        Integer rowsInDatabaseActual = countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 2 and course_id = 1 and group_id = 2 and teacher_id = 3 and auditory_id = 3 and  time_table_id = 2 and date = '2020-05-05'");
        assertEquals(rowsInDatabaseExpected, rowsInDatabaseActual);
    }

    @Test
    public void givenLessonId_whenGetById_thenGetLesson() {
        Integer lessonId = 2;
        Course course = Course.builder().id(2).build();
        Group group = Group.builder().id(1).build();
        Teacher teacher = Teacher.builder().id(2).build();
        TimeTable timeTable = TimeTable.builder().id(3).build();
        Auditory auditory = Auditory.builder().id(2).build();
        LocalDate date = LocalDate.parse("2020-09-01");
        Lesson expectedLesson = Lesson.builder().id(lessonId).course(course).group(group).teacher(teacher)
                .timeTable(timeTable).auditory(auditory).date(date).build();

        Lesson actuaLesson = lessonDao.getById(lessonId);

        assertEquals(expectedLesson, actuaLesson);
    }

    @Test
    public void givenLessonId_whenDelete_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "lessons");

        lessonDao.delete(2);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "lessons");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

    @Test
    public void givenGroup_whenFindByGroup_thenGetLessons() {
        Course course1 = Course.builder().id(1).build();
        Group group1 = Group.builder().id(3).build();
        Teacher teacher1 = Teacher.builder().id(1).build();
        TimeTable timeTable1 = TimeTable.builder().id(4).build();
        Auditory auditory1 = Auditory.builder().id(2).build();
        LocalDate date1 = LocalDate.parse("2020-07-03");
        Lesson lesson1 = Lesson.builder().id(5).course(course1).group(group1).teacher(teacher1).timeTable(timeTable1)
                .auditory(auditory1).date(date1).build();

        Course course2 = Course.builder().id(3).build();
        Group group2 = Group.builder().id(3).build();
        Teacher teacher2 = Teacher.builder().id(3).build();
        TimeTable timeTable2 = TimeTable.builder().id(2).build();
        Auditory auditory2 = Auditory.builder().id(2).build();
        LocalDate date2 = LocalDate.parse("2020-08-03");
        Lesson lesson2 = Lesson.builder().id(6).course(course2).group(group2).teacher(teacher2).timeTable(timeTable2)
                .auditory(auditory2).date(date2).build();
        List<Lesson> expectedLessons = asList(lesson1, lesson2);

        List<Lesson> actualLessons = lessonDao.getByGroup(group2, LocalDate.parse("2020-07-03"), LocalDate.parse("2020-08-03"));

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    public void givenTeacher_whenFindByTeacher_thenGetLessons() {
        Course course1 = Course.builder().id(3).build();
        Group group1 = Group.builder().id(2).build();
        Teacher teacher1 = Teacher.builder().id(3).build();
        TimeTable timeTable1 = TimeTable.builder().id(2).build();
        Auditory auditory1 = Auditory.builder().id(3).build();
        LocalDate date1 = LocalDate.parse("2020-09-02");
        Lesson lesson1 = Lesson.builder().id(3).course(course1).group(group1).teacher(teacher1).timeTable(timeTable1)
                .auditory(auditory1).date(date1).build();

        Course course2 = Course.builder().id(3).build();
        Group group2 = Group.builder().id(3).build();
        Teacher teacher2 = Teacher.builder().id(3).build();
        TimeTable timeTable2 = TimeTable.builder().id(2).build();
        Auditory auditory2 = Auditory.builder().id(2).build();
        LocalDate date2 = LocalDate.parse("2020-08-03");
        Lesson lesson2 = Lesson.builder().id(6).course(course2).group(group2).teacher(teacher2).timeTable(timeTable2)
                .auditory(auditory2).date(date2).build();
        List<Lesson> expectedLessons = asList(lesson1, lesson2);

        List<Lesson> actualLessons = lessonDao.getByTeacher(teacher2, LocalDate.parse("2020-08-03"), LocalDate.parse("2020-09-02"));

        assertEquals(expectedLessons, actualLessons);
    }
}
