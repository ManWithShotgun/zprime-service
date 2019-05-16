package ru.ilia.data.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.ilia.data.repository.ZprimeRepository;

import java.util.Map;

@Slf4j
@Repository
public class ZprimeRepositoryImpl implements ZprimeRepository {

    private HashOperations hashOperations;

    @Autowired
    public ZprimeRepositoryImpl(@Qualifier("redisTemplateBean") RedisTemplate template) {
        this.hashOperations = template.opsForHash();
    }

    @Override
    public void addResult(String key, String mass, String result) {
        log.info("Redis: add result - started");
        hashOperations.put(key, mass, result);
        log.info("Redis: add result - completed");
    }

    @Override
    public String getResult(String key, String id) {
        log.info("Redis: get result - started");
        Object o = hashOperations.get(key, id);
        log.info("Redis: get result - completed");
        return (String) o;
    }

    @Override
    public Map getAll(String key) {
        log.info("Redis: all result - started");
        Map entries = hashOperations.entries(key);
        log.info("Redis: all result - completed");
        return entries;
    }

    @Override
    public Long getSize(String key) {
        log.info("Redis: size result - started");
        Long size = hashOperations.size(key);
        log.info("Redis: size result - completed");
        return size;
    }


}
