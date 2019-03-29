package ru.ilia.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ilia.services.PythiaRequest;
import ru.ilia.services.PythiaService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// (cd app/pythia8226_export/zprime/ && ./calc_zprime 0.003 4710)
@Service
public class PythiaServiceImpl implements PythiaService {

    private static final Logger logger = LoggerFactory.getLogger(PythiaServiceImpl.class);
    public static final String PYTHIA_PATH_PROP = "PYTHIA_PATH";
    public static final String MODEL_DIR_PROP = "MODEL_DIR";
    public static final String PYTHIA_RUNNER_PROP = "PYTHIA_RUNNER";

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
    }

    @Override
    public String calculate(final PythiaRequest request) {
        String result = StringUtils.EMPTY;
        try {
            Process process = startProcess(request);
            logger.info("PID: has started: " + process.pid());
            result = getProcessResultSync(process.getInputStream());
            logger.info("Result: " + result);
            waitEndOfProcessSync(process);
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    private Process startProcess(PythiaRequest request) throws IOException {
        String[] commands = new String[]{PYTHIA_RUNNER, request.getKsi(), request.getMass()};
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        return processBuilder.start();
    }

    private String getProcessResultSync(InputStream processOutput) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(processOutput));
        String line;
        String lastLine = "";
        while ((line = reader.readLine()) != null) {
            lastLine = line;
        }
        return validateResult(lastLine);
    }

    // TODO: move the method into PythiaResult and throw exceptions
    private String validateResult(String result) {
        if (StringUtils.isBlank(result)) {
            logger.error("Calculated result is blank");
            return StringUtils.EMPTY;
        }
        if (!result.startsWith("Cross:")) {
            logger.error("Calculated result has incorrect format");
            return StringUtils.EMPTY;
        }
        String[] split = result.split("Cross:");
        String value = split[1];
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.error("Calculated result has incorrect format", e);
            return StringUtils.EMPTY;
        }
        return value;
    }


    /**
     * The method is useless when sync reader before was called
     */
    private void waitEndOfProcessSync(Process process) throws ExecutionException, InterruptedException {
        CompletableFuture<Process> onProcessExit = process.onExit();
        onProcessExit.get(); // sync mode
        onProcessExit.thenAccept(ph -> {
            // FIXME: move functionality to read output of process here
            // FIXME: use RandomAccess implementation for reading only last line with result of calculation
            logger.info("PID: has stopped: " + ph.pid());
        });
    }
}
