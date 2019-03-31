package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import ru.ilia.services.PythiaRequest;
import ru.ilia.services.PythiaService;
import ru.ilia.services.ZprimeService;

import java.util.List;

@Slf4j
@Service
public class ZprimeServiceImpl implements ZprimeService {

    private final PythiaService pythiaService;
    private final RedisTemplate template;
    // HashOperations<String, MyKey, MyValue> hashOperations = redisTemplate.opsForHash();
    private HashOperations aMap;

    @Autowired
    public ZprimeServiceImpl(PythiaService pythiaService, @Qualifier("redisT") RedisTemplate template) {
        this.pythiaService = pythiaService;
        this.template = template;
        this.aMap = template.opsForHash();
    }

//    MyKey key = new MyKey("Jhon", "+138129129113");
//        MyValue value = new MyValue("Pushkina street", "Moscow");
//        HashOperations<String, MyKey, MyValue> hashOperations = redisTemplate.opsForHash();
//        hashOperations.put("myKey", key, value);
//        MyValue mappedValue = hashOperations.get("myKey", key);
//        MyValue newValue = new MyValue("Tverskaya street", "Moscow");
//        hashOperations.putIfAbsent("myKey", key, newValue);

    // TODO: constant 'qqq'
    @Cacheable(value = "qqq", key = "{#ksi, #mass}")
    @Override
    public String getResult(final String ksi, final String mass) {
//        testRedis();
        // check in db
        String id = ksi + mass;
        // TODO: create useful volatile create/calculate value
//        if (aMap.hasKey("qqq", id)) {
//            String res = (String) aMap.get("qqq", id);
//            log.info(id + " already exists: " + res);
//            return res;
//        }
//        aMap.put("qqq", id, "-1");
        String res = (String) aMap.get("qqq", id);
        log.info("Before: " + id + " - Has " + res);
        // FIXME: think that it is useless functionality for current redis configuration
        template.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.watch("qqq");
                log.info("Start calc");
                String result = pythiaService.calculate(new PythiaRequest(ksi, mass));
                operations.multi();
                aMap.put("qqq", id, result);
                return operations.exec();
            }
        });
        // if empty - call calculate
//        pythiaService.calculate();
        res = (String) aMap.get("qqq", id);
        log.info(String.valueOf(aMap.size("qqq")));
        log.info(res);
        return res;
    }

//    private void testRedis() {
//        ZprimeItem zprimeItem = new ZprimeItem(213L, "qwe", "qqq", "qwe");
//        zprimeRepository.save(zprimeItem);
//        zprimeRepository.save(zprimeItem);
//        zprimeRepository.findAll().forEach(zprimeItem1 -> {
//            System.out.println(zprimeItem1.getId());
//        });
//    }
}
