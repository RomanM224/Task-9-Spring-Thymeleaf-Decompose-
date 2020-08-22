package com.maistruk.university.controller;

import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTable;
import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTable2;
import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTable3;
import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTableDto;
import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTableDto2;
import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTableId3;
import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTables;
import static com.maistruk.university.controller.TimeTableControllerTest.TestData.timeTables2;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import com.maistruk.university.config.TestConfig;
import com.maistruk.university.exceptions.TimeTableException;
import com.maistruk.university.mapper.TimeTableMapper;
import com.maistruk.university.model.TimeTable;
import com.maistruk.university.model.dto.TimeTableDto;
import com.maistruk.university.service.TimeTableService;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class TimeTableControllerTest {

    @Mock
    private TimeTableService timeTableService;
    @Mock
    private View mockView;
    @Mock
    private TimeTableMapper timeTableMapper;
    @InjectMocks
    private TimeTableController timeTableController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(timeTableController).setSingleView(mockView).build();
    }
    
    @Test
    public void givenShowAllURI_whenGetAll_thenSuccess() throws Exception {
        when(timeTableService.getAll()).thenReturn(timeTables);
        mockMvc.perform(get("/timetable/showAll"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("timeTables", timeTables))
               .andExpect(view().name("/timetable/showAll"));
    }
    
    @Test
    public void givenGetByIdURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/timetable/getById"))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/getById"));
    }
    
    @Test
    public void givenTimeTableId_whenGetById_thenSuccess() throws Exception {
        when(timeTableService.getById(2)).thenReturn(timeTable2);
        mockMvc.perform(post("/timetable/getById").param("id", "2"))
               .andExpect(model().attribute("timeTables", timeTables2))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/showAll"));
    }
    
    @Test
    public void givenCreateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/timetable/create"))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/create"));
    }
    
    @Test
    public void givenTimeTable_whenCreate_thenSuccess()  throws Exception{
        when(timeTableMapper.mapTimeTableDtoToTimeTable(timeTableDto)).thenReturn(timeTable);
        mockMvc.perform(post("/timetable/create").param("startTime", "09:00").param("finishTime", "10:20"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(timeTableService).create(timeTable);
    }
    
    @Test
    public void givenWrongTimeTable_whenCreate_thenThrowException()  throws Exception{
        when(timeTableMapper.mapTimeTableDtoToTimeTable(timeTableDto)).thenReturn(timeTable);
        doThrow(TimeTableException.class).when(timeTableService).create(timeTable);
        mockMvc.perform(post("/timetable/create").param("startTime", "09:00").param("finishTime", "10:20"))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/create"));
    }
    
    @Test
    public void givenUpdateURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/timetable/update"))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/update"));
    }
    
    @Test
    public void givenTimeTable_whenUpdate_thenSuccess()  throws Exception{
        when(timeTableMapper.mapTimeTableDtoToTimeTable(timeTableDto2)).thenReturn(timeTable3);
        mockMvc.perform(post("/timetable/update").param("id", "3").param("startTime", "12:20").param("finishTime", "13:40"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(timeTableService).update(timeTable3);
    }
    
    @Test
    public void givenWrongTimeTable_whenUpdate_thenThrowException()  throws Exception{
        when(timeTableMapper.mapTimeTableDtoToTimeTable(timeTableDto2)).thenReturn(timeTable3);
        doThrow(TimeTableException.class).when(timeTableService).update(timeTable3);
        mockMvc.perform(post("/timetable/update").param("id", "3").param("startTime", "12:20").param("finishTime", "13:40"))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/update"));
    }
    
    @Test
    public void givenDeleteURI_whenMockMVC_thenSuccess() throws Exception {
        mockMvc.perform(get("/timetable/delete"))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/delete"));
    }
    
    @Test
    public void givenTimeTableId_whenDelete_thenSuccess()  throws Exception{
        mockMvc.perform(post("/timetable/delete").param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(view().name("/info"));
        verify(timeTableService).delete(timeTableId3);
    }
    
    @Test
    public void givenWrongTimeTableId_whenDelete_thenThrowException()  throws Exception{
        doThrow(TimeTableException.class).when(timeTableService).delete(5);
        mockMvc.perform(post("/timetable/delete").param("id", "5"))
               .andExpect(status().isOk())
               .andExpect(view().name("/timetable/delete"));
    }

    interface TestData {

        Integer timeTableId = 1;
        Integer timeTableId2 = 2;
        Integer timeTableId3 = 3;
        Integer timeTableId4 = 4;
        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime finishTime = LocalTime.parse("10:20");

        TimeTable timeTable = TimeTable.builder().startTime(startTime).finishTime(finishTime).build();
        TimeTable timeTable2 = TimeTable.builder().id(timeTableId2).startTime(LocalTime.parse("10:40"))
                .finishTime(LocalTime.parse("12:00")).build();
        TimeTable timeTable3 = TimeTable.builder().id(timeTableId3).startTime(LocalTime.parse("12:20"))
                .finishTime(LocalTime.parse("13:40")).build();
        TimeTable timeTable4 = TimeTable.builder().id(timeTableId4).startTime(LocalTime.parse("14:00"))
                .finishTime(LocalTime.parse("15:20")).build();
        List<TimeTable> timeTables = asList(timeTable, timeTable2, timeTable3, timeTable4);
        List<TimeTable> timeTables2 = asList(timeTable2);

        TimeTableDto timeTableDto = TimeTableDto.builder().startTime(startTime).finishTime(finishTime).build();
        TimeTableDto timeTableDto2 = TimeTableDto.builder().id(timeTableId3).startTime(LocalTime.parse("12:20")).finishTime(LocalTime.parse("13:40")).build();

    }

}
