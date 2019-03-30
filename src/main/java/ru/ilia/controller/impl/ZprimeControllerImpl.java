package ru.ilia.controller.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ilia.controller.ZprimeController;
import ru.ilia.services.ZprimeService;

@Slf4j
@RestController
public class ZprimeControllerImpl implements ZprimeController {

    private final ZprimeService zprimeService;

    @Autowired
    public ZprimeControllerImpl(ZprimeService zprimeService) {
        this.zprimeService = zprimeService;
    }

    @GetMapping("/point")
    @Override
    public String getPoint(@RequestParam("ksi") String ksi, @RequestParam("mass") String mass) {
        if (StringUtils.isBlank(ksi) || StringUtils.isBlank(mass)) {
            log.warn("Ksi or mass is empty");
            throw new IllegalArgumentException("Ksi or mass is empty");
        }
        // TODO: the controller should use repository
//        PythiaRequest pythiaRequest = new PythiaRequest(ksi, mass);
//        pythiaService.calculate(pythiaRequest);
        zprimeService.getResult(ksi, mass);
        return "";
    }


}
