package ru.ilia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ilia.services.PythiaService;
import ru.ilia.services.PythiaRequest;

@RestController
public class ZprimeService {

    private static final Logger logger = LoggerFactory.getLogger(ZprimeService.class);

    @Autowired
    public PythiaService pythiaService;

    public int abc = 133;

    @RequestMapping("/")
    public String home() {
        send();
//         throw NumberFormatException
//        NumberUtils.createDouble("0.003");
        logger.warn("Test hot reload");
        logger.warn("das222");
        abc++;
        return "Hello Docker World" + abc;
    }

    public void send() {
        PythiaRequest pythiaRequest = new PythiaRequest("0.003", "4500");
        pythiaService.calculate(pythiaRequest);
        pythiaService.getResultFromFile(pythiaRequest);
    }
}
