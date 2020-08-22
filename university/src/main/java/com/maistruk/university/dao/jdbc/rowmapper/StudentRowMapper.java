package com.maistruk.university.dao.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.maistruk.university.model.Student;

@Component
public class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int row) throws SQLException {
        Integer id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        return Student.builder().id(id).firstName(firstName).lastName(lastName).build();
    }

}
