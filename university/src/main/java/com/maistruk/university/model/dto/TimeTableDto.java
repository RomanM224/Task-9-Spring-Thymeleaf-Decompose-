package com.maistruk.university.model.dto;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TimeTableDto {

    Integer id;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime finishTime;
}
