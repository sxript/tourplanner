package at.fhtw.swen2.tutorial.dal.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll();
    Optional<T> findById(Long id);
    Optional<T> update(T entity);
    T save(T entity);
    void delete(T entity);
}
