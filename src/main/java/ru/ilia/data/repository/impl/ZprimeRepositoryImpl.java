package ru.ilia.data.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ilia.data.repository.ZprimeRepository;
import ru.ilia.services.PythiaRequest;

import java.util.Map;

@Transactional // FIXME: think that it is useless functionality for current redis configuration
@Repository
public class ZprimeRepositoryImpl implements ZprimeRepository {

    private HashOperations hashOperations;

    @Autowired
    public ZprimeRepositoryImpl(@Qualifier("redisT") RedisTemplate template) {
        this.hashOperations = template.opsForHash();
    }

    @Override
    public void addResult(PythiaRequest pythiaRequest, String result) {
        hashOperations.put(pythiaRequest.getKsi(), pythiaRequest.getMass(), result);
    }

    @Override
    public String getResult(String key, String id) {
        return (String) hashOperations.get(key, id);
    }

    @Override
    public Map getAll(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Long getSize(String key) {
        return hashOperations.size(key);
    }


}
