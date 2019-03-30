package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ru.ilia.data.model.ZprimeItem;
import ru.ilia.data.repository.ZprimeRepository;
import ru.ilia.services.PythiaRequest;
import ru.ilia.services.PythiaService;
import ru.ilia.services.ZprimeService;

import java.util.List;

@Slf4j
@Service
public class ZprimeServiceImpl implements ZprimeService {

    private final PythiaService pythiaService;
    private final ZprimeRepository zprimeRepository;
    private final StringRedisTemplate template;
    // HashOperations<String, MyKey, MyValue> hashOperations = redisTemplate.opsForHash();
    private HashOperations<String, String, String> aMap;
    @Autowired
    public ZprimeServiceImpl(PythiaService pythiaService, ZprimeRepository zprimeRepository, @Qualifier("stringRedisTemplate") StringRedisTemplate template) {
        this.pythiaService = pythiaService;
        this.zprimeRepository = zprimeRepository;
        this.template = template;
        this.aMap  = template.opsForHash();
    }

//    MyKey key = new MyKey("Jhon", "+138129129113");
//        MyValue value = new MyValue("Pushkina street", "Moscow");
//        HashOperations<String, MyKey, MyValue> hashOperations = redisTemplate.opsForHash();
//        hashOperations.put("myKey", key, value);
//        MyValue mappedValue = hashOperations.get("myKey", key);
//        MyValue newValue = new MyValue("Tverskaya street", "Moscow");
//        hashOperations.putIfAbsent("myKey", key, newValue);

    @Override
    public String getResult(final String ksi, final String mass) {
//        testRedis();
        // check in db
        String id = ksi + mass;
        if (aMap.hasKey("qqq", id)) {
            String res = aMap.get("qqq", id);
            log.info(id + " already exists: " + res);
            return res;
        }
        template.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                String result = pythiaService.calculate(new PythiaRequest(ksi, mass));
                aMap.put("qqq", id, result);
                return operations.exec();
            }
        });
        // if empty - call calculate
//        pythiaService.calculate();
        log.info(String.valueOf(aMap.size("qqq")));
        return null;
    }

    private void testRedis() {
        ZprimeItem zprimeItem = new ZprimeItem(213L, "qwe", "qqq", "qwe");
        zprimeRepository.save(zprimeItem);
        zprimeRepository.save(zprimeItem);
        zprimeRepository.findAll().forEach(zprimeItem1 -> {
            System.out.println(zprimeItem1.getId());
        });
    }
}
