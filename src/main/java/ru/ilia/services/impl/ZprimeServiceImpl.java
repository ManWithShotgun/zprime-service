package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ilia.services.PythiaService;
import ru.ilia.services.ZprimeService;

@Slf4j
@Service
public class ZprimeServiceImpl implements ZprimeService {

    private final PythiaService pythiaService;

    @Autowired
    public ZprimeServiceImpl(PythiaService pythiaService) {
        this.pythiaService = pythiaService;
    }

    @Override
    public String getResult(final String ksi, final String mass) {
        // check in db
        // if empty - call calculate
//        pythiaService.calculate();
        return null;
    }
}
