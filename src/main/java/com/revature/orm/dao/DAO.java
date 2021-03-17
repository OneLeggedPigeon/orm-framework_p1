package com.revature.orm.dao;

import java.util.List;
import java.util.Optional;

/**
 * https://www.baeldung.com/java-dao-pattern
 * @param <T> Object Type
 */
public interface DAO<T> {

    Optional<T> get(long id);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
