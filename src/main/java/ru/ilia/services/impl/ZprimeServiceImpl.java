package ru.ilia.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ilia.services.PythiaService;
import ru.ilia.services.ZprimeService;

@Service
public class ZprimeServiceImpl implements ZprimeService {

    private static final Logger logger = LoggerFactory.getLogger(ZprimeServiceImpl.class);

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
