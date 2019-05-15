package ru.ilia.data.repository;

import java.util.Map;

/**
 * The my own realization repository but
 * Jedis has realization of {@code CrudRepository<T,V>} for use just extends the interface without realization as bean
 *
 * key -- is a 'table' name
 * id -- is a key of hash map
 * result -- is a value of hash map
 * */
public interface ZprimeRepository {

    void addResult(String ksi, String mass, String result);

    String getResult(String key, String id);

    Map getAll(String key);

    Long getSize(String key);

}
