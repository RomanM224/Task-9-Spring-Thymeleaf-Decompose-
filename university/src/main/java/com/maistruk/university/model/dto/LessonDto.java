package com.maistruk.university.model.dto;

import java.time.LocalDate;

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
public class LessonDto {
    
    Integer lessonId;
    Integer groupId;
    Integer courseId;
    Integer teacherId;
    Integer auditoryId;
    Integer timeTableId;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate date;
}
