package com.maistruk.university.dao;

import com.maistruk.university.model.Auditory;

public interface AuditoryDao extends Dao<Auditory> {

    public Auditory getByNumber(Integer number);

}
