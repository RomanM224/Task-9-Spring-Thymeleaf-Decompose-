package com.maistruk.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.maistruk.university.dao.TimeTableDao;
import com.maistruk.university.dao.jdbc.rowmapper.TimeTableRowMapper;
import com.maistruk.university.model.TimeTable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class JdbcTimeTableDao implements TimeTableDao {

    static final String ADD_TIME_TABLE = "INSERT INTO time_tables (id, start_time, finish_time) VALUES (DEFAULT, ?, ?)";
    static final String UPDATE_TIME_TABLE = "UPDATE time_tables SET start_time = ?, finish_time = ? WHERE id = ?";
    static final String GET_ALL_TIME_TABLES = "SELECT * FROM time_tables";
    static final String GET_TIME_TABLES_BY_ID = "SELECT * FROM time_tables WHERE id = ?";
    static final String GET_TIME_TABLES_BY_TIME = "SELECT * FROM time_tables WHERE start_time = ? AND finish_time = ?";
    static final String DELETE_TIME_TABLE = "DELETE FROM time_tables WHERE id = ?";
    static final String DELETE_LESSONS_RELATED_TO_TIME_TABLE = "DELETE FROM lessons WHERE time_table_id = ?";

    JdbcTemplate jdbcTemplate;
    TimeTableRowMapper timeTableRowMapper;

    @Override
    public void create(TimeTable timeTable) {
        log.info(String.format("Create time table (%s) [Dao layer]", timeTable.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_TIME_TABLE, new String[] { "id" });
            statement.setObject(1, timeTable.getStartTime());
            statement.setObject(2, timeTable.getFinishTime());
            return statement;
        }, keyHolder);
        timeTable.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(TimeTable timeTable) {
        log.info(String.format("Update time table (%s) [Dao layer]", timeTable.toString()));
        jdbcTemplate.update(UPDATE_TIME_TABLE, timeTable.getStartTime(), timeTable.getFinishTime(), timeTable.getId());
    }

    @Override
    public List<TimeTable> getAll() {
        log.info("Get all time tables [Dao layer]");
        return jdbcTemplate.query(GET_ALL_TIME_TABLES, timeTableRowMapper);
    }

    @Override
    public void delete(Integer id) {
        log.info(String.format("Delete time table by id=%d [Dao layer]", id));
        jdbcTemplate.update(DELETE_LESSONS_RELATED_TO_TIME_TABLE, id);
        jdbcTemplate.update(DELETE_TIME_TABLE, id);
    }

    @Override
    public TimeTable getById(Integer id) {
        log.info(String.format("Get time table by id=%d [Dao layer]", id));
        List<TimeTable> timeTables = jdbcTemplate.query(GET_TIME_TABLES_BY_ID, timeTableRowMapper, id);
        if (timeTables.isEmpty()) {
            return null;
        }
        return timeTables.get(0);
    }

    @Override
    public TimeTable getByTime(LocalTime startTime, LocalTime finishTime) {
        log.info(String.format("Get time table by start time (%s) and finish time (%s) [Dao layer]",
                startTime.toString(), finishTime.toString()));
        List<TimeTable> timeTables = jdbcTemplate.query(GET_TIME_TABLES_BY_TIME, timeTableRowMapper, startTime,
                finishTime);
        if (timeTables.isEmpty()) {
            return null;
        }
        return timeTables.get(0);
    }
}
