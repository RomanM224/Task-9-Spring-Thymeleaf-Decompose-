package com.maistruk.university.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.maistruk.university.dao.AuditoryDao;
import com.maistruk.university.exceptions.AuditoryException;
import com.maistruk.university.model.Auditory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class AuditoryService {

    AuditoryDao auditoryDao;

    public void create(Auditory auditory) throws AuditoryException {
        log.info(String.format("Create auditory (%s) [Service layer]", auditory.toString()));
        if (auditoryDao.getByNumber(auditory.getNumber()) != null) {
            throw new AuditoryException("Auditory with this number already exist");
        }
        auditoryDao.create(auditory);
    }

    public void update(Auditory auditory) throws AuditoryException {
        if (auditory == null) {
            log.info("Update auditory [Service layer]");
            throw new AuditoryException("Auditory does not exist");
        }
        log.info(String.format("Update auditory (%s) [Service layer]", auditory.toString()));
        if (auditoryDao.getByNumber(auditory.getNumber()) != null) {
            throw new AuditoryException("Auditory with this number already exist");
        }
        auditoryDao.update(auditory);
    }

    public void delete(Integer id)  throws AuditoryException {
        log.info(String.format("Delete auditory by id=%d [Service layer]", id));
        if(auditoryDao.getById(id) == null) {
            throw new AuditoryException("Auditory does not exist");
        }
        auditoryDao.delete(id);
    }

    public List<Auditory> getAll() {
        log.info("Get all auditories [Service layer]");
        return auditoryDao.getAll();
    }

    public Auditory getById(Integer id) {
        log.info(String.format("Get auditory by id=%d [Service layer]", id));
        return auditoryDao.getById(id);
    }

}
