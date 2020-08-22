package com.maistruk.university.mapper;

import org.springframework.stereotype.Component;

import com.maistruk.university.model.TimeTable;
import com.maistruk.university.model.dto.TimeTableDto;

@Component
public class TimeTableMapper {

    public TimeTable mapTimeTableDtoToTimeTable(TimeTableDto timeTableDto) {
        return TimeTable.builder().id(timeTableDto.getId()).startTime(timeTableDto.getStartTime())
                .finishTime(timeTableDto.getFinishTime()).build();
    }
}
