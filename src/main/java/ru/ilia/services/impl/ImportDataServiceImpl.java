package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.ilia.data.repository.ZprimeRepository;
import ru.ilia.services.ImportDataService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Service
public class ImportDataServiceImpl implements ImportDataService {

    private final ZprimeRepository zprimeRepository;

    @Autowired
    public ImportDataServiceImpl(ZprimeRepository zprimeRepository) {
        this.zprimeRepository = zprimeRepository;
    }


    @Override
    public void importData(String ksi, String events, String cycles) throws IOException {
        StringBuilder path = new StringBuilder("data/");
        path.append(ksi);
        path.append("_");
        path.append(events);
        path.append("_");
        path.append(cycles);
        path.append(".txt");
        log.info("Import data from: " + path.toString());
        String key = ksi + events + cycles;
        InputStream resource = new ClassPathResource(path.toString()).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
            String line = reader.readLine();
            while (line != null) {
                line = StringUtils.normalizeSpace(line);
                String[] strings = line.split(StringUtils.SPACE);
                zprimeRepository.addResult(key, strings[0], strings[1]);
                log.info("Added line: " + key + " :: " + strings[0] + " " + strings[1]);
                line = reader.readLine();
            }
        }
    }
}
