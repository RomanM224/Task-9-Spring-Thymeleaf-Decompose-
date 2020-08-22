package com.maistruk.university.service;

import static com.maistruk.university.service.AuditoryServiceTest.TestData.auditories;
import static com.maistruk.university.service.AuditoryServiceTest.TestData.auditory;
import static com.maistruk.university.service.AuditoryServiceTest.TestData.auditory2;
import static com.maistruk.university.service.AuditoryServiceTest.TestData.auditoryNumber;
import static com.maistruk.university.service.AuditoryServiceTest.TestData.notExistAuditory;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.maistruk.university.dao.jdbc.JdbcAuditoryDao;
import com.maistruk.university.exceptions.AuditoryException;
import com.maistruk.university.model.Auditory;

@ExtendWith(MockitoExtension.class)
public class AuditoryServiceTest {

    @Mock
    private JdbcAuditoryDao auditoryDao;
    @InjectMocks
    private AuditoryService auditoryService;

    @Test
    public void givenAuditory_whenCreate_thenCreated() throws AuditoryException {
        when(auditoryDao.getByNumber(auditoryNumber)).thenReturn(null);

        auditoryService.create(auditory);

        verify(auditoryDao).create(auditory);
    }

    @Test
    public void givenAuditoryWithExistNumber_whenCreate_thenNotCreated() {
        when(auditoryDao.getByNumber(auditoryNumber)).thenReturn(auditory);

        assertThrows(AuditoryException.class, () -> auditoryService.create(auditory));
    }

    @Test
    public void givenAuditory_whenUpdate_thenUpdated() throws AuditoryException {
        when(auditoryDao.getByNumber(auditoryNumber)).thenReturn(null);

        auditoryService.update(auditory);

        verify(auditoryDao).update(auditory);
    }

    @Test
    public void givenNotExistAuditory_whenUpdate_thenNotUpdated() throws AuditoryException {

        assertThrows(AuditoryException.class, () -> auditoryService.update(notExistAuditory));
    }

    @Test
    public void givenAuditoryWithExistNumber_whenUpdate_thenNotUpdated() {
        when(auditoryDao.getByNumber(auditoryNumber)).thenReturn(auditory);

        assertThrows(AuditoryException.class, () -> auditoryService.update(auditory));
    }

    @Test
    public void whenGetAll_thenGetAllAuditories() {
        when(auditoryDao.getAll()).thenReturn(auditories);

        List<Auditory> actualAuditories = auditoryService.getAll();

        assertEquals(auditories, actualAuditories);
    }

    @Test
    public void givenAuditoryId_whenGetById_thenGetAuditory() {
        Integer id = 2;
        when(auditoryDao.getById(id)).thenReturn(auditory2);

        Auditory actualAuditory = auditoryService.getById(id);

        assertEquals(auditory2, actualAuditory);
    }

    interface TestData {

        Integer auditoryId = 1;
        Integer auditoryId2 = 2;
        Integer auditoryId3 = 3;
        Integer auditoryNumber = 100;

        Auditory auditory = Auditory.builder().id(auditoryId).number(auditoryNumber).build();
        Auditory auditory2 = Auditory.builder().id(auditoryId2).number(101).build();
        Auditory auditory3 = Auditory.builder().id(auditoryId3).number(102).build();
        Auditory notExistAuditory = null;

        List<Auditory> auditories = asList(auditory, auditory2, auditory3);
    }

}
