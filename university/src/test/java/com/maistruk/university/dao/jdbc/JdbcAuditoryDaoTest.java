package com.maistruk.university.dao.jdbc;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.maistruk.university.config.TestConfig;
import com.maistruk.university.model.Auditory;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcAuditoryDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcAuditoryDao auditoryDao;

    @Test
    public void givenNewAuditory_whenCreate_thenCreatedAndGetGeneratedId() {
        Auditory expectedAuditory = Auditory.builder().id(4).number(103).build();
        Auditory actualAuditory = Auditory.builder().number(103).build();

        auditoryDao.create(actualAuditory);

        assertEquals(expectedAuditory, actualAuditory);
    }

    @Test
    public void givenExistingAuditory_whenUpdate_thenUpdated() {
        Auditory expectedAuditory = Auditory.builder().id(3).number(456).build();

        auditoryDao.update(expectedAuditory);

        Integer rowsInDatabaseExpected = 1;
        Integer rowsInDatabaseActual = countRowsInTableWhere(jdbcTemplate, "auditories", "number = 456 and id = 3");
        assertEquals(rowsInDatabaseExpected, rowsInDatabaseActual);
    }

    @Test
    public void whenGetAll_thenGetAllAuditories() {
        Auditory auditory1 = Auditory.builder().id(1).number(100).build();
        Auditory auditory2 = Auditory.builder().id(2).number(101).build();
        Auditory auditory3 = Auditory.builder().id(3).number(102).build();
        List<Auditory> expectedAuditories = asList(auditory1, auditory2, auditory3);

        List<Auditory> actualAuditories = auditoryDao.getAll();

        assertEquals(expectedAuditories, actualAuditories);
    }

    @Test
    public void givenAuditoryId_whenGetById_thenGetAuditory() {
        Integer id = 2;
        Auditory expectedAuditory = Auditory.builder().id(id).number(101).build();

        Auditory actualAuditory = auditoryDao.getById(id);

        assertEquals(expectedAuditory, actualAuditory);
    }

    @Test
    public void givenAuditoryId_whenDelete_thenDeleted() {
        Integer rowsBeforeDelete = countRowsInTable(jdbcTemplate, "auditories");

        auditoryDao.delete(2);

        Integer rowsAfterDelete = countRowsInTable(jdbcTemplate, "auditories");
        assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
    }

}
