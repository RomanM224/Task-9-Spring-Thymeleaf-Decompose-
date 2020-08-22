package com.maistruk.university.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maistruk.university.dao.LessonDao;
import com.maistruk.university.exceptions.LessonException;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class LessonService {

    LessonDao lessonDao;
    GroupService groupService;
    CourseService courseService;
    AuditoryService auditoryService;
    TeacherService teacherService;
    TimeTableService timeTableService;

    public void create(Lesson lesson) throws LessonException {
        log.info("Create lesson [Service layer]");
        fillLessonFields(lesson);
        if (!groupService.getCoursesByGroup(lesson.getGroup()).stream()
                .filter(course -> course.getId().equals(lesson.getCourse().getId())).findFirst().isPresent()) {
            throw new LessonException(String.format("Group (%s) do not contain course (%s)",
                    lesson.getGroup().toString(), lesson.getCourse().toStringIdName()));
        }
        if (!teacherService.getByCourse(lesson.getCourse()).stream()
                .filter(teacher -> teacher.getId().equals(lesson.getTeacher().getId())).findFirst().isPresent()) {
            throw new LessonException(String.format("Teacher (%s) do not contain course (%s)",
                    lesson.getTeacher().toString(), lesson.getCourse().toStringIdName()));
        }
        if ((lesson.getDate().getDayOfWeek() == DayOfWeek.SATURDAY)
                || (lesson.getDate().getDayOfWeek() == DayOfWeek.SUNDAY)) {
            throw new LessonException("Wrong date, day of week is saturday or sunday");
        }
        if (lessonDao.getByGroupTimeTableDate(lesson.getGroup(), lesson.getTimeTable(), lesson.getDate()) != null) {
            throw new LessonException("This group have lesson at that time");
        }
        if (lessonDao.getByAuditoryTimeTableDate(lesson.getAuditory(), lesson.getTimeTable(),
                lesson.getDate()) != null) {
            throw new LessonException("Auditory is reserved at that time");
        }
        if (lessonDao.getByTeacherTimeTableDate(lesson.getTeacher(), lesson.getTimeTable(), lesson.getDate()) != null) {
            throw new LessonException("This teacher have lesson at that time");
        }
        log.info(String.format("Lesson created (%s) [Service layer]", lesson.toString()));
        lessonDao.create(lesson);

    }

    public void update(Lesson lesson) throws LessonException {
        log.info("Update lesson [Service layer]");
        if (lessonDao.getById(lesson.getId()) == null) {
            throw new LessonException(String.format("Lesson (id=%d) does not exist", lesson.getId()));
        }
        fillLessonFields(lesson);
        if (!groupService.getCoursesByGroup(lesson.getGroup()).stream()
                .filter(course -> course.getId().equals(lesson.getCourse().getId())).findFirst().isPresent()) {
            throw new LessonException(String.format("Group (%s) do not contain course (%s)",
                    lesson.getGroup().toString(), lesson.getCourse().toStringIdName()));
        }
        if (!teacherService.getByCourse(lesson.getCourse()).stream()
                .filter(teacher -> teacher.getId().equals(lesson.getTeacher().getId())).findFirst().isPresent()) {
            throw new LessonException(String.format("Teacher (%s) do not contain course (%s)",
                    lesson.getTeacher().toString(), lesson.getCourse().toStringIdName()));
        }
        if ((lesson.getDate().getDayOfWeek() == DayOfWeek.SATURDAY)
                || (lesson.getDate().getDayOfWeek() == DayOfWeek.SUNDAY)) {
            throw new LessonException("Wrong date, day of week is saturday or sunday");
        }
        if (lessonDao.getByGroupTimeTableDate(lesson.getGroup(), lesson.getTimeTable(), lesson.getDate()) != null) {
            throw new LessonException("This group have lesson at that time");
        }
        if (lessonDao.getByAuditoryTimeTableDate(lesson.getAuditory(), lesson.getTimeTable(),
                lesson.getDate()) != null) {
            throw new LessonException("Auditory is reserved at that time");
        }
        if (lessonDao.getByTeacherTimeTableDate(lesson.getTeacher(), lesson.getTimeTable(), lesson.getDate()) != null) {
            throw new LessonException("This teacher have lesson at that time");
        }
        log.info(String.format("Update lesson (%s) [Service layer]", lesson.toString()));
        lessonDao.update(lesson);
    }

    public void delete(Integer id) throws LessonException {
        log.info(String.format("Delete lesson by id=%d [Service layer]", id));
        if (lessonDao.getById(id) == null) {
            throw new LessonException("Lesson does not exist");
        }
        lessonDao.delete(id);
    }

    public List<Lesson> getAll() {
        log.info("Get all lessons [Service layer]");
        List<Lesson> lessons = lessonDao.getAll();
        lessons.forEach(lesson -> {
            lesson.setCourse(courseService.getById(lesson.getCourse().getId()));
            lesson.setGroup(groupService.getById(lesson.getGroup().getId()));
            lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
            lesson.setTimeTable(timeTableService.getById(lesson.getTimeTable().getId()));
            lesson.setAuditory(auditoryService.getById(lesson.getAuditory().getId()));
        });
        return lessons;
    }

    public Lesson getById(Integer id) {
        log.info(String.format("Get lesson by id=%d [Service layer]", id));
        Lesson lesson = lessonDao.getById(id);
        if (lesson == null) {
            return lesson;
        }
        lesson.setCourse(courseService.getById(lesson.getCourse().getId()));
        lesson.setGroup(groupService.getById(lesson.getGroup().getId()));
        lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
        lesson.setTimeTable(timeTableService.getById(lesson.getTimeTable().getId()));
        lesson.setAuditory(auditoryService.getById(lesson.getAuditory().getId()));
        return lesson;
    }

    public List<Lesson> getByGroup(Group group, LocalDate startDate, LocalDate finishDate) throws LessonException {
        log.info("Get lessons by group [Service layer]");
        if (groupService.getById(group.getId()) == null) {
            throw new LessonException(String.format("Group (id=%d) does not exist", group.getId()));
        }
        if (startDate == null || finishDate == null) {
            throw new LessonException("Date is empty");
        }
        log.info(String.format("Get lessons by group (%s) [Service layer]", group.toString()));
        List<Lesson> lessons = lessonDao.getByGroup(group, startDate, finishDate);
        lessons.forEach(lesson -> {
            lesson.setCourse(courseService.getById(lesson.getCourse().getId()));
            lesson.setGroup(groupService.getById(lesson.getGroup().getId()));
            lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
            lesson.setTimeTable(timeTableService.getById(lesson.getTimeTable().getId()));
            lesson.setAuditory(auditoryService.getById(lesson.getAuditory().getId()));
        });
        return lessons;
    }

    public List<Lesson> getByTeacher(Teacher teacher, LocalDate startDate, LocalDate finishDate) throws LessonException {
        log.info("Get lessons by teacher [Service layer]");
        if (teacherService.getById(teacher.getId()) == null) {
            throw new LessonException(String.format("Teacher (id=%d) does not exist", teacher.getId()));
        }
        if (startDate == null  || finishDate == null) {
            throw new LessonException("Date is empty");
        }
        log.info(String.format("Get lessons by teacher (%s) [Service layer]", teacher.toString()));
        List<Lesson> lessons = lessonDao.getByTeacher(teacher, startDate, finishDate);
        lessons.forEach(lesson -> {
            lesson.setCourse(courseService.getById(lesson.getCourse().getId()));
            lesson.setGroup(groupService.getById(lesson.getGroup().getId()));
            lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
            lesson.setTimeTable(timeTableService.getById(lesson.getTimeTable().getId()));
            lesson.setAuditory(auditoryService.getById(lesson.getAuditory().getId()));
        });
        return lessons;
    }
    
    public void fillLessonFields(Lesson lesson) throws LessonException {
        Integer id = lesson.getGroup().getId();
        lesson.setGroup(groupService.getById(lesson.getGroup().getId()));
        if (lesson.getGroup() == null) {
            throw new LessonException(String.format("Group (id=%d) does not exist", id));
        }
        id = lesson.getCourse().getId();
        lesson.setCourse(courseService.getById(lesson.getCourse().getId())); 
        if (lesson.getCourse() == null) {
            throw new LessonException(String.format("Course (id=%d) does not exist", id));
        }
        id = lesson.getTeacher().getId();
        lesson.setTeacher(teacherService.getById(lesson.getTeacher().getId()));
        if (lesson.getTeacher() == null) {
            throw new LessonException(String.format("Teacher (id=%d) does not exist", id));
        }
        id = lesson.getAuditory().getId();
        lesson.setAuditory(auditoryService.getById(lesson.getAuditory().getId()));
        if (lesson.getAuditory() == null) {
            throw new LessonException(String.format("Auditory (id=%d) does not exist", id));
        }
        id = lesson.getTimeTable().getId();
        lesson.setTimeTable(timeTableService.getById(lesson.getTimeTable().getId()));
        if (lesson.getTimeTable() == null) {
            throw new LessonException(String.format("Time table (id=%d) does not exist", id));
        }
        if (lesson.getDate() == null) {
            throw new LessonException("Date is empty");
        }
    }

}
