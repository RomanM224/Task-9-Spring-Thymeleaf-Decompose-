package com.maistruk.university.dao.jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.maistruk.university.dao.AuditoryDao;
import com.maistruk.university.dao.jdbc.rowmapper.AuditoryRowMapper;
import com.maistruk.university.model.Auditory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class JdbcAuditoryDao implements AuditoryDao {

    static final String ADD_AUDITORY = "INSERT INTO auditories (id, number) VALUES(DEFAULT, ?)";
    static final String UPDATE_AUDITORY = "UPDATE auditories SET number = ? WHERE id = ?";
    static final String DELETE_AUDITORY = "DELETE FROM auditories WHERE id = ?";
    static final String DELETE_LESSONS_RELATED_TO_AUDITORY = "DELETE FROM lessons WHERE auditory_id = ?";
    static final String GET_ALL_AUDITORY = "SELECT * FROM auditories";
    static final String GET_AUDITORY_BY_ID = "SELECT * FROM auditories WHERE id = ?";
    static final String GET_AUDITORY_BY_NUMBER = "SELECT * FROM auditories WHERE number = ?";

    JdbcTemplate jdbcTemplate;
    AuditoryRowMapper auditoryRowMapper;

    @Override
    public void create(Auditory auditory) {
        log.info(String.format("Create auditory (%s) [Dao layer]", auditory.toString()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_AUDITORY, new String[] { "id" });
            statement.setInt(1, auditory.getNumber());
            return statement;
        }, keyHolder);
        auditory.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(Auditory auditory) {
        log.info(String.format("Update auditory (%s) [Dao layer]", auditory.toString()));
        jdbcTemplate.update(UPDATE_AUDITORY, auditory.getNumber(), auditory.getId());
    }

    @Override
    public List<Auditory> getAll() {
        log.info("Get all auditories [Dao layer]");
        return jdbcTemplate.query(GET_ALL_AUDITORY, auditoryRowMapper);
    }

    @Override
    public void delete(Integer id) {
        log.info(String.format("Delete auditory by id=%d [Dao layer]", id));
        jdbcTemplate.update(DELETE_LESSONS_RELATED_TO_AUDITORY, id);
        jdbcTemplate.update(DELETE_AUDITORY, id);
    }

    @Override
    public Auditory getById(Integer id) {
        log.info(String.format("Get auditory by id=%d [Dao layer]", id));
        List<Auditory> auditories = jdbcTemplate.query(GET_AUDITORY_BY_ID, auditoryRowMapper, id);
        if (auditories.isEmpty()) {
            return null;
        }
        return auditories.get(0);
    }

    @Override
    public Auditory getByNumber(Integer number) {
        log.info(String.format("Get auditory by number=%d [Dao layer]", number));
        List<Auditory> auditories = jdbcTemplate.query(GET_AUDITORY_BY_NUMBER, auditoryRowMapper, number);
        if (auditories.isEmpty()) {
            return null;
        }
        return auditories.get(0);
    }

}
