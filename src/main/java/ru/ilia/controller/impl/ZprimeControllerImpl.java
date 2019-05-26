package ru.ilia.controller.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ilia.controller.ZprimeController;
import ru.ilia.services.ColisionExpectedWithLinesService;
import ru.ilia.services.ImportDataService;
import ru.ilia.services.ZprimeService;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
public class ZprimeControllerImpl implements ZprimeController {

    private final ZprimeService zprimeService;
    private final ImportDataService importDataService;
    private final ColisionExpectedWithLinesService colisionExpectedWithLinesService;

    @Autowired
    public ZprimeControllerImpl(ZprimeService zprimeService, ImportDataService importDataService,
                                ColisionExpectedWithLinesService colisionExpectedWithLinesService) {
        this.zprimeService = zprimeService;
        this.importDataService = importDataService;
        this.colisionExpectedWithLinesService = colisionExpectedWithLinesService;
    }

    @GetMapping("/point")
    @Override
    public String getPoint(
        @RequestParam("ksi") String ksi,
        @RequestParam("mass") String mass,
        @RequestParam("events") String events,
        @RequestParam("recalc") String recalc
    ) {
        if (StringUtils.isBlank(ksi) || StringUtils.isBlank(mass)) {
            log.warn("Ksi or mass is empty");
            throw new IllegalArgumentException("Ksi or mass is empty");
        }
        log.info("Cache: startCache");
        String result = zprimeService.getResult(ksi, mass, events, recalc);
        log.info("Cache: endCache");
        return result;
    }

    @GetMapping("/import")
    @Override
    public String importData(
        @RequestParam("ksi") String ksi,
        @RequestParam("events") String events,
        @RequestParam("recalc") String recalc
    ) {
        try {
            importDataService.importData(ksi, events, recalc);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "Import was failed";
        }
        log.info("Imported");
        return "Imported";
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/collisions")
    @Override
    public ResponseEntity<?> getResultCollisionPoints() {
        try {
            Map<String, String> collisions = colisionExpectedWithLinesService.getCollisions();
            log.info("Returns collisions: " + collisions.size());
            return ResponseEntity.ok(collisions);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
