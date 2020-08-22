package com.maistruk.university.service;

import static com.maistruk.university.service.LessonServiceTest.TestData.auditory;
import static com.maistruk.university.service.LessonServiceTest.TestData.auditory2;
import static com.maistruk.university.service.LessonServiceTest.TestData.auditoryId;
import static com.maistruk.university.service.LessonServiceTest.TestData.auditoryId2;
import static com.maistruk.university.service.LessonServiceTest.TestData.course;
import static com.maistruk.university.service.LessonServiceTest.TestData.course2;
import static com.maistruk.university.service.LessonServiceTest.TestData.courseId;
import static com.maistruk.university.service.LessonServiceTest.TestData.courseId2;
import static com.maistruk.university.service.LessonServiceTest.TestData.courseTeachers;
import static com.maistruk.university.service.LessonServiceTest.TestData.date;
import static com.maistruk.university.service.LessonServiceTest.TestData.dateSaturday;
import static com.maistruk.university.service.LessonServiceTest.TestData.dateSunday;
import static com.maistruk.university.service.LessonServiceTest.TestData.group;
import static com.maistruk.university.service.LessonServiceTest.TestData.groupCourses;
import static com.maistruk.university.service.LessonServiceTest.TestData.groupId;
import static com.maistruk.university.service.LessonServiceTest.TestData.groupLessons;
import static com.maistruk.university.service.LessonServiceTest.TestData.lesson;
import static com.maistruk.university.service.LessonServiceTest.TestData.lesson2;
import static com.maistruk.university.service.LessonServiceTest.TestData.lessonId;
import static com.maistruk.university.service.LessonServiceTest.TestData.notExistAuditory;
import static com.maistruk.university.service.LessonServiceTest.TestData.notExistCourse;
import static com.maistruk.university.service.LessonServiceTest.TestData.notExistGroup;
import static com.maistruk.university.service.LessonServiceTest.TestData.notExistLesson;
import static com.maistruk.university.service.LessonServiceTest.TestData.notExistTeacher;
import static com.maistruk.university.service.LessonServiceTest.TestData.notExistTimeTable;
import static com.maistruk.university.service.LessonServiceTest.TestData.teacher;
import static com.maistruk.university.service.LessonServiceTest.TestData.teacher2;
import static com.maistruk.university.service.LessonServiceTest.TestData.teacherId;
import static com.maistruk.university.service.LessonServiceTest.TestData.teacherId2;
import static com.maistruk.university.service.LessonServiceTest.TestData.timeTable;
import static com.maistruk.university.service.LessonServiceTest.TestData.timeTable2;
import static com.maistruk.university.service.LessonServiceTest.TestData.timeTableId;
import static com.maistruk.university.service.LessonServiceTest.TestData.timeTableId2;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maistruk.university.dao.jdbc.JdbcLessonDao;
import com.maistruk.university.exceptions.LessonException;
import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    private JdbcLessonDao lessonDao;
    @Mock
    private GroupService groupService;
    @Mock
    private CourseService courseService;
    @Mock
    private AuditoryService auditoryService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private TimeTableService timeTableService;
    @InjectMocks
    private LessonService lessonService;

    @Test
    public void givenLesson_whenCreate_thenCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByAuditoryTimeTableDate(auditory, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByTeacherTimeTableDate(teacher, timeTable, date)).thenReturn(notExistLesson);

        lessonService.create(lesson);

        verify(lessonDao).create(lesson);

    }

    @Test
    public void givenLessonWithNotExistingGroup_whenCreate_thenNotCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(notExistGroup);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonWithNotExistingCourse_whenCreate_thenNotCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(notExistCourse);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonWithNotExistingTeacher_whenCreate_thenNotCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(notExistTeacher);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonWithNotExistingAuditory_whenCreate_thenNotCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(notExistAuditory);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonWithNotExistingTimeTable_whenCreate_thenNotCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(notExistTimeTable);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonDateDayOfWeekSaturday_whenCreate_thenNotCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(dateSaturday).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonDateDayOfWeekSunday_whenCreate_thenNotCreated() throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(dateSunday).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonWithGroupThatAlreadyExistInThisTimeAndDate_whenCreate_thenNotCreated()
            throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(lesson2);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonWithAuditoryThatAlreadyExistInThisTimeAndDate_whenCreate_thenNotCreated()
            throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByAuditoryTimeTableDate(auditory, timeTable, date)).thenReturn(lesson2);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLessonWithTeacherThatAlreadyExistInThisTimeAndDate_whenCreate_thenNotCreated()
            throws LessonException {
        Lesson lesson = Lesson.builder().group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByAuditoryTimeTableDate(auditory, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByTeacherTimeTableDate(teacher, timeTable, date)).thenReturn(lesson2);

        assertThrows(LessonException.class, () -> lessonService.create(lesson));
    }

    @Test
    public void givenLesson_whenUpdate_thenUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByAuditoryTimeTableDate(auditory, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByTeacherTimeTableDate(teacher, timeTable, date)).thenReturn(notExistLesson);

        lessonService.update(lesson);

        verify(lessonDao).update(lesson);
    }

    @Test
    public void givenLessonWithNotExistingGroup_whenUpdate_thenNotUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher)
                .auditory(auditory).timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(notExistGroup);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonWithNotExistingCourse_whenUpdate_thenNotUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher)
                .auditory(auditory).timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(notExistCourse);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonWithNotExistingTeacher_whenUpdate_thenNotUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher)
                .auditory(auditory).timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(notExistTeacher);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonWithNotExistingAuditory_whenUpdate_thenNotUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher)
                .auditory(auditory).timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(notExistAuditory);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonWithNotExistingTimeTable_whenUpdate_thenNotUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(notExistTimeTable);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonDateDayOfWeekSaturday_whenUpdate_thenNotUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(dateSaturday).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonDateDayOfWeekSunday_whenUpdate_thenNotUpdated() throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(dateSunday).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonWithGroupThatAlreadyExistInThisTimeAndDate_whenUpdate_thenNotUpdated()
            throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(lesson2);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonWithAuditoryThatAlreadyExistInThisTimeAndDate_whenUpdate_thenNotUpdated()
            throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByAuditoryTimeTableDate(auditory, timeTable, date)).thenReturn(lesson2);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonWithTeacherThatAlreadyExistInThisTimeAndDate_whenUpdate_thenNotUpdated()
            throws LessonException {
        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        when(lessonDao.getById(lessonId)).thenReturn(lesson2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(groupService.getCoursesByGroup(group)).thenReturn(groupCourses);
        when(teacherService.getByCourse(course)).thenReturn(courseTeachers);
        when(lessonDao.getByGroupTimeTableDate(group, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByAuditoryTimeTableDate(auditory, timeTable, date)).thenReturn(notExistLesson);
        when(lessonDao.getByTeacherTimeTableDate(teacher, timeTable, date)).thenReturn(lesson2);

        assertThrows(LessonException.class, () -> lessonService.update(lesson));
    }

    @Test
    public void givenLessonId_whenGetById_thenGetLesson() {
        when(lessonDao.getById(lessonId)).thenReturn(lesson);
        when(groupService.getById(groupId)).thenReturn(group);
        when(courseService.getById(courseId)).thenReturn(course);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);

        Lesson actuaLesson = lessonService.getById(lessonId);

        assertEquals(lesson, actuaLesson);

    }

    @Test
    public void givenGroup_whenFindByGroup_thenGetLessons() throws LessonException {
        when(courseService.getById(courseId)).thenReturn(course);
        when(courseService.getById(courseId2)).thenReturn(course2);
        when(groupService.getById(groupId)).thenReturn(group);
        when(teacherService.getById(teacherId)).thenReturn(teacher);
        when(teacherService.getById(teacherId2)).thenReturn(teacher2);
        when(timeTableService.getById(timeTableId)).thenReturn(timeTable);
        when(timeTableService.getById(timeTableId2)).thenReturn(timeTable2);
        when(auditoryService.getById(auditoryId)).thenReturn(auditory);
        when(auditoryService.getById(auditoryId2)).thenReturn(auditory2);
        when(lessonDao.getByGroup(group, date, date)).thenReturn(groupLessons);

        List<Lesson> actualLessons = lessonService.getByGroup(group, date, date);

        assertEquals(groupLessons, actualLessons);
    }

    interface TestData {
        LocalDate date = LocalDate.parse("2020-09-02");
        LocalDate dateSaturday = LocalDate.parse("2020-09-05");
        LocalDate dateSunday = LocalDate.parse("2020-09-06");
        Integer lessonId = 1;
        Integer groupId = 2;
        Integer courseId = 3;
        Integer teacherId = 4;
        Integer auditoryId = 5;
        Integer timeTableId = 6;

        Integer lessonId2 = 7;
        Integer courseId2 = 8;
        Integer teacherId2 = 9;
        Integer auditoryId2 = 10;
        Integer timeTableId2 = 11;

        Lesson notExistLesson = null;
        Group notExistGroup = null;
        Course notExistCourse = null;
        Teacher notExistTeacher = null;
        Auditory notExistAuditory = null;
        TimeTable notExistTimeTable = null;

        Group group = Group.builder().id(groupId).name("FJ-52").build();
        Course course = Course.builder().id(courseId).name("Computer science").description(
                "Computer science is the study of processes that interact with data and that can be represented as data in the form of programs.")
                .build();
        Course course2 = Course.builder().id(courseId2).name("Database").description(
                "Database is the study of an organized collection of data, generally stored and accessed electronically from a computer system.")
                .build();
        Teacher teacher = Teacher.builder().id(teacherId).firstName("Peter").lastName("Russo").build();
        Teacher teacher2 = Teacher.builder().id(teacherId2).firstName("Zoe").lastName("Barnes").build();
        Auditory auditory = Auditory.builder().id(auditoryId).number(100).build();
        Auditory auditory2 = Auditory.builder().id(auditoryId2).number(101).build();
        TimeTable timeTable = TimeTable.builder().id(timeTableId).startTime(LocalTime.parse("10:40"))
                .finishTime(LocalTime.parse("12:00")).build();
        TimeTable timeTable2 = TimeTable.builder().id(timeTableId2).startTime(LocalTime.parse("09:00"))
                .finishTime(LocalTime.parse("10:20")).build();

        Lesson lesson = Lesson.builder().id(lessonId).group(group).course(course).teacher(teacher).auditory(auditory)
                .timeTable(timeTable).date(date).build();
        Lesson lesson2 = Lesson.builder().id(lessonId2).group(group).course(course2).teacher(teacher2)
                .auditory(auditory2).timeTable(timeTable2).date(date).build();

        List<Lesson> groupLessons = asList(lesson, lesson2);
        List<Teacher> courseTeachers = asList(teacher);
        List<Course> groupCourses = asList(course, course2);

    }

}
