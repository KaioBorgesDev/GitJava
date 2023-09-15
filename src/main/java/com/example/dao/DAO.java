package com.example.dao;

import java.util.List;

public interface DAO<T> {

    Object get(Long id);

    int save(T t);

    boolean update(T t, String[] params);

    boolean delete(T t);

}
