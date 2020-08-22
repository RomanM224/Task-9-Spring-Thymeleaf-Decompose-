package com.maistruk.university.dao;

import java.time.LocalDate;
import java.util.List;

import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;

public interface LessonDao extends Dao<Lesson> {

    List<Lesson> getByGroup(Group group, LocalDate startDate, LocalDate finishDate);

    List<Lesson> getByTeacher(Teacher teacher, LocalDate startDate, LocalDate finishDate);

    Lesson getByGroupTimeTableDate(Group group, TimeTable timeTable, LocalDate date);

    Lesson getByAuditoryTimeTableDate(Auditory auditory, TimeTable timeTable, LocalDate date);

    Lesson getByTeacherTimeTableDate(Teacher teacher, TimeTable timeTable, LocalDate date);

}
