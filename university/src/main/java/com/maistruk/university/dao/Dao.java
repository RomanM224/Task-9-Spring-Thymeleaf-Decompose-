package com.maistruk.university.dao;

import java.util.List;

public interface Dao<T> {

    void create(T object);

    List<T> getAll();

    void update(T object);

    void delete(Integer id);

    T getById(Integer id);
}
