package com.maistruk.university.dao.jdbc.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.maistruk.university.model.Group;

@Component
public class GroupRowMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int row) throws SQLException {
        Integer id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return Group.builder().id(id).name(name).build();
    }

}
