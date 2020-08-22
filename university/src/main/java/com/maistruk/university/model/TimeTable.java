package com.maistruk.university.model;

import java.time.LocalTime;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTable {

    Integer id;
    LocalTime startTime;
    LocalTime finishTime;

    @Override
    public String toString() {
        startTime = Stream.of(startTime).filter(time -> time != null).findFirst().orElse(LocalTime.parse("00:00"));
        finishTime = Stream.of(finishTime).filter(time -> time != null).findFirst().orElse(LocalTime.parse("00:00"));
        return String.format("id=%-3d | Start time:%-15s | Finish time:%-15s |", id, startTime.toString(),
                finishTime.toString());
    }

}
