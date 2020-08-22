package com.maistruk.university.dao.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.maistruk.university.model.Auditory;

@Component
public class AuditoryRowMapper implements RowMapper<Auditory> {

    @Override
    public Auditory mapRow(ResultSet resultSet, int row) throws SQLException {
        Integer id = resultSet.getInt("id");
        Integer number = resultSet.getInt("number");
        return Auditory.builder().id(id).number(number).build();
    }

}
