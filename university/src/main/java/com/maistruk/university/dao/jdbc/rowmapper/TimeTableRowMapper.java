package com.maistruk.university.dao.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.maistruk.university.model.TimeTable;

@Component
public class TimeTableRowMapper implements RowMapper<TimeTable> {

    @Override
    public TimeTable mapRow(ResultSet resultSet, int row) throws SQLException {
        Integer id = resultSet.getInt("id");
        LocalTime startTime = LocalTime.parse(resultSet.getString("start_time"));
        LocalTime finishTime = LocalTime.parse(resultSet.getString("finish_time"));
        return TimeTable.builder().id(id).startTime(startTime).finishTime(finishTime).build();
    }

}
