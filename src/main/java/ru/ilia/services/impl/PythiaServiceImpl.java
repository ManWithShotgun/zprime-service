package ru.ilia.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ilia.services.PythiaService;
import ru.ilia.services.PythiaRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// (cd app/pythia8226_export/zprime/ && ./calc_zprime 0.003 4710)
@Service
public class PythiaServiceImpl implements PythiaService {

    private static final Logger logger = LoggerFactory.getLogger(PythiaServiceImpl.class);
    public static final String PYTHIA_PATH_PROP = "PYTHIA_PATH";
    public static final String MODEL_DIR_PROP = "MODEL_DIR";
    public static final String PYTHIA_RUNNER_PROP = "PYTHIA_RUNNER";

    private final String PYTHIA_RESULT_FORMAT;
    private final String PYTHIA_RUNNER;

    @Autowired
    public PythiaServiceImpl(Environment environment) {
        if (environment.getProperty(PYTHIA_PATH_PROP) == null) {
            // if empty then exception
//            throw new RuntimeException("Path to sh should be defined");
        }
        logger.info("env: " + environment.getProperty(PYTHIA_PATH_PROP));
        String pythiaPath = environment.getProperty(PYTHIA_PATH_PROP);
        String modelDir = environment.getProperty(MODEL_DIR_PROP);
        // read data from folder PYTHIA_PATH + MODEL_DIR
        // table_{corner}.txt
        // table_0.003.txt -> 0.00572394 4710 0.003
        this.PYTHIA_RUNNER = environment.getProperty(PYTHIA_RUNNER_PROP);
        this.PYTHIA_RESULT_FORMAT = pythiaPath + modelDir + "/table_%s.txt";
    }

    @Override
    public void calculate(PythiaRequest request) {
        try {
            String[] commands = new String[]{PYTHIA_RUNNER, request.getKsi(), request.getMass()};
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
//            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = processBuilder.start();
            logger.info("PID: has started: " + process.pid());
            CompletableFuture<Process> onProcessExit = process.onExit();
            process = onProcessExit.get(); // sync mode
            onProcessExit.thenAccept(ph -> {
                logger.info("PID: has stopped: " + ph.pid());
            });
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public String getResultFromFile(PythiaRequest request) {
        Map<String, String> results = readResultsFromFile(String.format(PYTHIA_RESULT_FORMAT, request.getKsi()));
        String result = results.get(request.getMass());
        if (StringUtils.isEmpty(result)) {
            logger.error(String.format("Result for mass %s not found", request));
            return StringUtils.EMPTY;
        }
        return result;
    }

    private Map<String, String> readResultsFromFile(String path) {
        Path pathToFile = Path.of(path);
        Map<String, String> results = new HashMap();
        if (!Files.exists(pathToFile)) {
            logger.error(String.format("File %s doest not exist", path));
            return results;
        }
        try {
            Files.readAllLines(pathToFile).forEach(
                    line -> {
                        String[] split = line.split(StringUtils.SPACE);
                        results.put(split[1], split[0]);
                    }
            );
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return results;
    }
}
