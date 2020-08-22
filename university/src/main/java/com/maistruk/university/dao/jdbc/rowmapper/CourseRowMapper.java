package com.maistruk.university.dao.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.maistruk.university.model.Course;

@Component
public class CourseRowMapper implements RowMapper<Course> {

    @Override
    public Course mapRow(ResultSet resultSet, int row) throws SQLException {
        Integer id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        return Course.builder().id(id).name(name).description(description).build();
    }

}
