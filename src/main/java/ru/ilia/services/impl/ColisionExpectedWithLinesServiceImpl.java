package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.ilia.services.ColisionExpectedWithLinesService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ColisionExpectedWithLinesServiceImpl implements ColisionExpectedWithLinesService {

    // returns key: mass, value: ksi
    @Override
    public Map<String, String> getCollisions() throws IOException {
        Map<String, String> collisions = new HashMap<>();
        StringBuilder path = new StringBuilder("data/zprime-expected-ssm.txt");
        log.info("Get collisions from file: " + path.toString());
        InputStream resource = new ClassPathResource(path.toString()).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
            String line = reader.readLine();
            while (line != null) {
                line = StringUtils.normalizeSpace(line);
                String[] strings = line.split(StringUtils.SPACE);
                log.info("collision: mass: " + strings[0] + " | ksi: " + strings[1]);
                collisions.put(strings[0], strings[1]);
                line = reader.readLine();
            }
        }
        return collisions;
    }
}
