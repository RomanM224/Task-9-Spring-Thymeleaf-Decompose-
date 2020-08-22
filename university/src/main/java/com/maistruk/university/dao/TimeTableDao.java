package com.maistruk.university.dao;

import java.time.LocalTime;

import com.maistruk.university.model.TimeTable;

public interface TimeTableDao extends Dao<TimeTable> {

    public TimeTable getByTime(LocalTime startTime, LocalTime finishTime);

}
