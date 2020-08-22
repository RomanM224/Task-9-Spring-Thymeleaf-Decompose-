package com.maistruk.university.dao.jdbc;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalTime;
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
import com.maistruk.university.model.TimeTable;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcTimeTableDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcTimeTableDao timeTableDoa;

    @Test
    public void givenNewTimeTable_whenCreate_thenCreatedAndGetGeneratedId() {
        TimeTable expectedTimeTable = TimeTable.builder().id(5).startTime(LocalTime.parse("16:00"))
                .finishTime(LocalTime.parse("17:00")).build();
        TimeTable actualTimeTable = TimeTable.builder().startTime(LocalTime.parse("16:00"))
                .finishTime(LocalTime.parse("17:00")).build();

        timeTableDoa.create(actualTimeTable);

        assertEquals(expectedTimeTable, actualTimeTable);
    }

    @Test
    public void givenExistingTimeTable_whenUpdate_thenUpdated() {
        TimeTable expectedTimeTable = TimeTable.builder().id(2).startTime(LocalTime.parse("20:00"))
                .finishTime(LocalTime.parse("22:00")).build();
        timeTableDoa.update(expectedTimeTable);

        Integer rowsInDatabaseExpected = 1;
        Integer rowsInDatabaseActual = countRowsInTableWhere(jdbcTemplate, "time_tables",
                "start_time = '20:00' and finish_time = '22:00' and id = 2");
        assertEquals(rowsInDatabaseExpected, rowsInDatabaseActual);
    }

    @Test
    public void whenGetAll_thenGetAllTimeTables() {
        TimeTable timeTable1 = TimeTable.builder().id(1).startTime(LocalTime.parse("09:00"))
                .finishTime(LocalTime.parse("10:20")).build();
        TimeTable timeTable2 = TimeTable.builder().id(2).startTime(LocalTime.parse("10:40"))
                .finishTime(LocalTime.parse("12:00")).build();
        TimeTable timeTable3 = TimeTable.builder().id(3).startTime(LocalTime.parse("12:20"))
                .finishTime(LocalTime.parse("13:40")).build();
        TimeTable timeTable4 = TimeTable.builder().id(4).startTime(LocalTime.parse("14:00"))
                .finishTime(LocalTime.parse("15:20")).build();
        List<TimeTable> expectedTimeTables = asList(timeTable1, timeTable2, timeTable3, timeTable4);

        List<TimeTable> actualTimeTables = timeTableDoa.getAll();

        assertEquals(expectedTimeTables, actualTimeTables);
    }

    @Test
    public void givenTimeTableId_whenGetById_thenGetTimeTable() {
        Integer id = 2;
        TimeTable expectedTimeTable = TimeTable.builder().id(id).startTime(LocalTime.parse("10:40"))
                .finishTime(LocalTime.parse("12:00")).build();

        TimeTable actualTimeTable = timeTableDoa.getById(id);

        assertEquals(expectedTimeTable, actualTimeTable);
    }

    @Test
    public void givenTimeTableId_whenDelete_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "time_tables");

        timeTableDoa.delete(2);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "time_tables");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

}
