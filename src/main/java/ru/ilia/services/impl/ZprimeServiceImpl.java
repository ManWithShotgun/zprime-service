package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Cacheable(value = "cache", key = "{#ksi, #mass, #countOfPythiaEvents, #countOfRecalculate}")
    @Override
    public String getResult(final String ksi, final String mass, final String countOfPythiaEvents, final String countOfRecalculate) {
        int calculateCycles = Integer.parseInt(countOfRecalculate);
        String result = null;
        // TODO: create useful volatile create/calculate value
        if (calculateCycles == 1 ) {
            PythiaRequest pythiaRequest = new PythiaRequest(ksi, mass, countOfPythiaEvents, "-1");
            result = pythiaService.calculate(pythiaRequest);
        } else {
            log.info("Runed cycle calc: " + calculateCycles);
            result = calculateInCycleAverage(ksi, mass, countOfPythiaEvents, calculateCycles);
            log.info("Finish cycle calc. result: " + result);
        }
        zprimeRepository.addResult(ksi + countOfPythiaEvents + countOfRecalculate, mass, result);
        log.info(zprimeRepository.getResult(ksi + countOfPythiaEvents + countOfRecalculate, mass));
        log.info("Size: " + zprimeRepository.getSize(ksi + countOfPythiaEvents + countOfRecalculate));
        return result;
    }

    private String calculateInCycleAverage(final String ksi, final String mass, final String countOfPythiaEvents, final int calculateCycles) {
        double sum = 0;
        for (int i = -1; i <= calculateCycles; i++) {
            PythiaRequest pythiaRequest = new PythiaRequest(ksi, mass, countOfPythiaEvents, Integer.toString(i));
            log.info("- cycle calc step: " + i);
            sum += Double.parseDouble(pythiaService.calculate(pythiaRequest));
        }
        return Double.toString(sum/calculateCycles);
    }

    @Override
    public Map<String, String> getAllResults(String ksi, String countOfPythiaEvents, String countOfRecalculate) {
        Map result = zprimeRepository.getAll(ksi + countOfPythiaEvents + countOfRecalculate);
        log.info("Size: " + zprimeRepository.getSize(ksi + countOfPythiaEvents + countOfRecalculate));
        return result;
    }
}
