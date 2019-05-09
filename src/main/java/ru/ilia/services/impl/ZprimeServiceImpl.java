package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.ilia.data.repository.ZprimeRepository;
import ru.ilia.services.PythiaRequest;
import ru.ilia.services.PythiaService;
import ru.ilia.services.ZprimeService;

import java.util.Map;

@Slf4j
@Service
public class ZprimeServiceImpl implements ZprimeService {

    private final ZprimeRepository zprimeRepository;
    private final PythiaService pythiaService;

    @Autowired
    public ZprimeServiceImpl(ZprimeRepository zprimeRepository, PythiaService pythiaService) {
        this.zprimeRepository = zprimeRepository;
        this.pythiaService = pythiaService;
    }

    @Cacheable(value = "cache", key = "{#ksi, #mass}")
    @Override
    public String getResult(final String ksi, final String mass) {
        PythiaRequest pythiaRequest = new PythiaRequest(ksi, mass);
        // TODO: create useful volatile create/calculate value
        String result = pythiaService.calculate(pythiaRequest);
        zprimeRepository.addResult(pythiaRequest, result);
        log.info(zprimeRepository.getResult(ksi, mass));
        log.info("Size: " + zprimeRepository.getSize(ksi));
        return result;
    }

    @Override
    public Map<String, String> getAllResults(String ksi) {
        Map result = zprimeRepository.getAll(ksi);
        log.info("Size: " + zprimeRepository.getSize(ksi));
        return result;
    }
}
