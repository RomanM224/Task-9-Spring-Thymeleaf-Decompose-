package com.maistruk.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.maistruk.university.dao.LessonDao;
import com.maistruk.university.dao.jdbc.rowmapper.LessonRowMapper;
import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class JdbcLessonDao implements LessonDao {

    static final String ADD_LESSON = "INSERT INTO lessons (id, course_id, group_id, teacher_id, auditory_id, time_table_id, date) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) ";
    static final String UPDATE_LESSON = "UPDATE lessons SET course_id = ?, group_id = ?, teacher_id = ?, auditory_id = ?, time_table_id = ?, date = ? WHERE id = ? ";
    static final String DELETE_LESSON = "DELETE FROM lessons WHERE id = ?";
    static final String GET_ALL_LESSON = "SELECT * FROM lessons";
    static final String GET_LESSON_BY_ID = "SELECT * FROM lessons WHERE id = ?";
    static final String GET_LESSON_BY_GROUP_AND_DATE = "SELECT * FROM lessons WHERE group_id = ? AND date >= ? AND date <= ?";
    static final String GET_LESSON_BY_TEACHER_AND_DATE = "SELECT * FROM lessons WHERE teacher_id = ? AND date >= ? AND date <= ?";
    static final String GET_LESSON_BY_GROUP_TIMETABLE_DATE = "SELECT * FROM lessons WHERE group_id = ? AND time_table_id = ? AND date = ?";
    static final String GET_LESSON_BY_AUDITORY_TIMETABLE_DATE = "SELECT * FROM lessons WHERE auditory_id = ? AND time_table_id = ? AND date = ?";
    static final String GET_LESSON_BY_TEACHER_TIMETABLE_DATE = "SELECT * FROM lessons WHERE teacher_id = ? AND time_table_id = ? AND date = ?";

    JdbcTemplate jdbcTemplate;
    LessonRowMapper lessonRowMapper;

    @Override
    public void create(Lesson lesson) {
        log.info(String.format("Create lesson (%s) [Dao layer]", lesson.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_LESSON, new String[] { "id" });
            statement.setInt(1, lesson.getCourse().getId());
            statement.setInt(2, lesson.getGroup().getId());
            statement.setInt(3, lesson.getTeacher().getId());
            statement.setInt(4, lesson.getAuditory().getId());
            statement.setInt(5, lesson.getTimeTable().getId());
            statement.setObject(6, lesson.getDate());
            return statement;
        }, keyHolder);
        lesson.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(Lesson lesson) {
        log.info(String.format("Update lesson (%s) [Dao layer]", lesson.toString()));
        jdbcTemplate.update(UPDATE_LESSON, lesson.getCourse().getId(), lesson.getGroup().getId(),
                lesson.getTeacher().getId(), lesson.getAuditory().getId(), lesson.getTimeTable().getId(),
                lesson.getDate(), lesson.getId());
    }

    @Override
    public void delete(Integer id) {
        log.info(String.format("Delete lesson by id=%d [Dao layer]", id));
        jdbcTemplate.update(DELETE_LESSON, id);
    }

    @Override
    public List<Lesson> getAll() {
        log.info("Get all lessons [Dao layer]");
        return jdbcTemplate.query(GET_ALL_LESSON, lessonRowMapper);
    }

    @Override
    public Lesson getById(Integer id) {
        log.info(String.format("Get lesson by id=%d [Dao layer]", id));
        List<Lesson> lessons = jdbcTemplate.query(GET_LESSON_BY_ID, lessonRowMapper, id);
        if (lessons.isEmpty()) {
            return null;
        }
        return lessons.get(0);
    }

    @Override
    public List<Lesson> getByGroup(Group group, LocalDate startDate, LocalDate finishDate) {
        log.info(String.format("Get lessons by group (%s) [Dao layer]", group.toString()));
        return jdbcTemplate.query(GET_LESSON_BY_GROUP_AND_DATE, lessonRowMapper, group.getId(), startDate, finishDate);
    }

    @Override
    public List<Lesson> getByTeacher(Teacher teacher, LocalDate startDate, LocalDate finishDate) {
        log.info(String.format("Get lessons by teacher (%s) [Dao layer]", teacher.toString()));
        return jdbcTemplate.query(GET_LESSON_BY_TEACHER_AND_DATE, lessonRowMapper, teacher.getId(), startDate, finishDate);
    }

    @Override
    public Lesson getByGroupTimeTableDate(Group group, TimeTable timeTable, LocalDate date) {
        log.info(String.format("Get lessons by group (%s), time table (%s) and date (%s)[Dao layer]", group.toString(),
                timeTable.toString(), date.toString()));
        List<Lesson> lessons = jdbcTemplate.query(GET_LESSON_BY_GROUP_TIMETABLE_DATE, lessonRowMapper, group.getId(),
                timeTable.getId(), date);
        if (lessons.isEmpty()) {
            return null;
        }
        return lessons.get(0);
    }

    @Override
    public Lesson getByAuditoryTimeTableDate(Auditory auditory, TimeTable timeTable, LocalDate date) {
        log.info(String.format("Get lessons by auditory (%s), time table (%s) and date (%s)[Dao layer]",
                auditory.toString(), timeTable.toString(), date.toString()));
        List<Lesson> lessons = jdbcTemplate.query(GET_LESSON_BY_AUDITORY_TIMETABLE_DATE, lessonRowMapper,
                auditory.getId(), timeTable.getId(), date);
        if (lessons.isEmpty()) {
            return null;
        }
        return lessons.get(0);
    }

    @Override
    public Lesson getByTeacherTimeTableDate(Teacher teacher, TimeTable timeTable, LocalDate date) {
        log.info(String.format("Get lessons by teacher (%s), time table (%s) and date (%s)[Dao layer]",
                teacher.toString(), timeTable.toString(), date.toString()));
        List<Lesson> lessons = jdbcTemplate.query(GET_LESSON_BY_TEACHER_TIMETABLE_DATE, lessonRowMapper,
                teacher.getId(), timeTable.getId(), date);
        if (lessons.isEmpty()) {
            return null;
        }
        return lessons.get(0);
    }

}
