package com.maistruk.university.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.maistruk.university.exceptions.TimeTableException;
import com.maistruk.university.mapper.TimeTableMapper;
import com.maistruk.university.model.TimeTable;
import com.maistruk.university.model.dto.TimeTableDto;
import com.maistruk.university.service.TimeTableService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/timetable")
public class TimeTableController {

    TimeTableService timeTableService;
    TimeTableMapper timeTableMapper;

    public TimeTableController(TimeTableService timeTableService, TimeTableMapper timeTableMapper) {
        this.timeTableService = timeTableService;
        this.timeTableMapper = timeTableMapper;
    }

    @GetMapping(value = "/showAll")
    public ModelAndView showTimeTables() {
        ModelAndView modelAndView = new ModelAndView("/timetable/showAll");
        List<TimeTable> timeTables = timeTableService.getAll();
        return modelAndView.addObject("timeTables", timeTables);
    }

    @GetMapping(value = "/getById")
    public ModelAndView getById() {
        return new ModelAndView("/timetable/getById");
    }

    @RequestMapping("/getById")
    public ModelAndView showById(@RequestParam("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("/timetable/showAll");
        TimeTable timeTable = timeTableService.getById(id);
        if (timeTable == null) {
            return modelAndView.addObject("timeTables", emptyList());
        }
        List<TimeTable> timeTables = asList(timeTable);
        return modelAndView.addObject("timeTables", timeTables);
    }
    
    @GetMapping(value = "/create")
    public ModelAndView create() {
        return new ModelAndView("/timetable/create");
    }
    
    @RequestMapping("/create")
    public ModelAndView create(@ModelAttribute TimeTableDto timeTableDto) {
        try {
            TimeTable timeTable = timeTableMapper.mapTimeTableDtoToTimeTable(timeTableDto);
            timeTableService.create(timeTable);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Time table created");
        } catch (TimeTableException exception) {
            ModelAndView modelAndView = new ModelAndView("/timetable/create");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/update")
    public ModelAndView update() {
        return new ModelAndView("/timetable/update");
    }
    
    @RequestMapping("/update")
    public ModelAndView update(@ModelAttribute TimeTableDto timeTableDto) {
        try {
            TimeTable timeTable = timeTableMapper.mapTimeTableDtoToTimeTable(timeTableDto);
            timeTableService.update(timeTable);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Time table updated");
        } catch (TimeTableException exception) {
            ModelAndView modelAndView = new ModelAndView("/timetable/update");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
    
    @GetMapping(value = "/delete")
    public ModelAndView delete() {
        return new ModelAndView("/timetable/delete");
    }
    
    @RequestMapping("/delete")
    public ModelAndView update(@RequestParam("id") Integer id) {

        try {
            timeTableService.delete(id);
            ModelAndView modelAndView = new ModelAndView("/info");
            return modelAndView.addObject("info", "Time table deleted");
        } catch (TimeTableException exception) {
            ModelAndView modelAndView = new ModelAndView("/timetable/delete");
            return modelAndView.addObject("info", exception.getMessage());
        }
    }
}
