package ru.ilia.controller.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ilia.controller.ZprimeController;
import ru.ilia.data.model.ZprimeItem;
import ru.ilia.data.repository.ZprimeRepository;
import ru.ilia.services.PythiaRequest;
import ru.ilia.services.PythiaService;

@RestController
public class ZprimeControllerImpl implements ZprimeController {

    private static final Logger logger = LoggerFactory.getLogger(ZprimeControllerImpl.class);

    private final PythiaService pythiaService;

    @Autowired
    private ZprimeRepository zprimeRepository;

    @Autowired
    public ZprimeControllerImpl(PythiaService pythiaService) {
        this.pythiaService = pythiaService;
    }

    @GetMapping("/point")
    @Override
    public String getPoint(@RequestParam("ksi") String ksi, @RequestParam("mass") String mass) {
        if (StringUtils.isBlank(ksi) || StringUtils.isBlank(mass)) {
            logger.warn("Ksi or mass is empty");
            throw new IllegalArgumentException("Ksi or mass is empty");
        }
        // TODO: the controller should use repository
        PythiaRequest pythiaRequest = new PythiaRequest(ksi, mass);
        pythiaService.calculate(pythiaRequest);
        return pythiaService.getResultFromFile(pythiaRequest);
//        testRedis();
//        return "";
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
