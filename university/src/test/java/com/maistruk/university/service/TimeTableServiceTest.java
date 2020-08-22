package com.maistruk.university.service;

import static com.maistruk.university.service.TimeTableServiceTest.TestData.finishTime;
import static com.maistruk.university.service.TimeTableServiceTest.TestData.notExistTimeTable;
import static com.maistruk.university.service.TimeTableServiceTest.TestData.startTime;
import static com.maistruk.university.service.TimeTableServiceTest.TestData.timeTable;
import static com.maistruk.university.service.TimeTableServiceTest.TestData.timeTable2;
import static com.maistruk.university.service.TimeTableServiceTest.TestData.timeTableId;
import static com.maistruk.university.service.TimeTableServiceTest.TestData.timeTables;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maistruk.university.dao.jdbc.JdbcTimeTableDao;
import com.maistruk.university.exceptions.TimeTableException;
import com.maistruk.university.model.TimeTable;

@ExtendWith(MockitoExtension.class)
public class TimeTableServiceTest {

    @Mock
    private JdbcTimeTableDao timeTableDao;
    @InjectMocks
    private TimeTableService timeTableService;

    @Test
    public void givenTimeTable_whenCreate_thenCreated() throws TimeTableException {
        when(timeTableDao.getByTime(startTime, finishTime)).thenReturn(notExistTimeTable);

        timeTableService.create(timeTable);

        verify(timeTableDao).create(timeTable);
    }

    @Test
    public void givenExistTimeTable_whenCreate_thenNotCreated() {
        when(timeTableDao.getByTime(startTime, finishTime)).thenReturn(timeTable);

        assertThrows(TimeTableException.class, () -> timeTableService.create(timeTable));
    }

    @Test
    public void givenTimeTable_whenUpdate_thenUpdated() throws TimeTableException {
        when(timeTableDao.getById(timeTableId)).thenReturn(timeTable);
        when(timeTableDao.getByTime(startTime, finishTime)).thenReturn(notExistTimeTable);

        timeTableService.update(timeTable);

        verify(timeTableDao).update(timeTable);
    }

    @Test
    public void givenNotExistTimeTable_whenUpdate_thenNotUpdated() {
        when(timeTableDao.getById(timeTableId)).thenReturn(notExistTimeTable);
        
        assertThrows(TimeTableException.class, () -> timeTableService.update(timeTable));
    }

    @Test
    public void givenExistTimeTable_whenUpdate_thenNotUpdated() {
        when(timeTableDao.getById(timeTableId)).thenReturn(timeTable);
        when(timeTableDao.getByTime(startTime, finishTime)).thenReturn(timeTable);

        assertThrows(TimeTableException.class, () -> timeTableService.update(timeTable));
    }

    @Test
    public void whenGetAll_thenGetAllTimeTables() {
        when(timeTableDao.getAll()).thenReturn(timeTables);

        List<TimeTable> actualTimeTables = timeTableService.getAll();

        assertEquals(timeTables, actualTimeTables);
    }

    @Test
    public void givenTimeTableId_whenGetById_thenGetTimeTable() {
        Integer id = 2;
        when(timeTableDao.getById(id)).thenReturn(timeTable2);

        TimeTable actualTimeTable = timeTableService.getById(id);

        assertEquals(timeTable2, actualTimeTable);
    }

    interface TestData {

        Integer timeTableId = 1;
        Integer timeTableId2 = 2;
        Integer timeTableId3 = 3;
        Integer timeTableId4 = 4;
        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime finishTime = LocalTime.parse("10:20");

        TimeTable timeTable = TimeTable.builder().id(timeTableId).startTime(startTime).finishTime(finishTime).build();
        TimeTable timeTable2 = TimeTable.builder().id(timeTableId2).startTime(LocalTime.parse("10:40"))
                .finishTime(LocalTime.parse("12:00")).build();
        TimeTable timeTable3 = TimeTable.builder().id(timeTableId3).startTime(LocalTime.parse("12:20"))
                .finishTime(LocalTime.parse("13:40")).build();
        TimeTable timeTable4 = TimeTable.builder().id(timeTableId4).startTime(LocalTime.parse("14:00"))
                .finishTime(LocalTime.parse("15:20")).build();
        TimeTable notExistTimeTable = null;
        List<TimeTable> timeTables = asList(timeTable, timeTable2, timeTable3, timeTable4);

    }

}
