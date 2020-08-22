package com.maistruk.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maistruk.university.dao.TimeTableDao;
import com.maistruk.university.exceptions.TimeTableException;
import com.maistruk.university.model.TimeTable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TimeTableService {

    TimeTableDao timeTableDao;

    public void create(TimeTable timeTable) throws TimeTableException {
        log.info(String.format("Create time table (%s) [Service layer]", timeTable.toString()));
        if (timeTable.getStartTime() == null || timeTable.getFinishTime() == null) {
            throw new TimeTableException("Start time or finish time is empty");
        }
        if (timeTableDao.getByTime(timeTable.getStartTime(), timeTable.getFinishTime()) != null) {
            throw new TimeTableException("This time table already exist");
        }
        timeTableDao.create(timeTable);
    }

    public void update(TimeTable timeTable) throws TimeTableException {
        if (timeTableDao.getById(timeTable.getId()) == null) {
            log.info("Update time table [Service layer]");
            throw new TimeTableException("Time table not exist");
        }
        if (timeTable.getStartTime() == null || timeTable.getFinishTime() == null) {
            throw new TimeTableException("Start time or finish time is empty");
        }
        if (timeTableDao.getByTime(timeTable.getStartTime(), timeTable.getFinishTime()) != null) {
            log.info("Update time table [Service layer]");
            throw new TimeTableException("This time table already exist");
        }
        log.info(String.format("Update time table (%s) [Service layer]", timeTable.toString()));
        timeTableDao.update(timeTable);
    }

    public void delete(Integer id) throws TimeTableException {
        log.info(String.format("Delete time table by id=%d [Service layer]", id));
        if (timeTableDao.getById(id) == null) {
            throw new TimeTableException("Time table not exist");
        }
        timeTableDao.delete(id);
    }

    public List<TimeTable> getAll() {
        log.info("Get all time tables [Service layer]");
        return timeTableDao.getAll();
    }

    public TimeTable getById(Integer id) {
        log.info(String.format("Get time table by id=%d [Service layer]", id));
        return timeTableDao.getById(id);
    }

}
