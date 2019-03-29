package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.ilia.services.PythiaProperties;
import ru.ilia.services.PythiaRequest;
import ru.ilia.services.PythiaService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// (cd app/pythia8226_export/zprime/ && ./calc_zprime 0.003 4710)
@Slf4j
@Service
@EnableConfigurationProperties(PythiaProperties.class)
public class PythiaServiceImpl implements PythiaService {

    // TODO: concat the fields in yaml file
    private final String PYTHIA_RESULT_FORMAT;
    private final String PYTHIA_RUNNER;

    @Autowired
    public PythiaServiceImpl(PythiaProperties properties) {
        if (properties.getPythiaPath() == null) {
            // if empty then exception
//            throw new RuntimeException("Path to sh should be defined");
        }
        log.info("env: " + properties.getPythiaPath());
        String pythiaPath = properties.getPythiaPath();
        String modelDir = properties.getModelDir();
        // read data from folder PYTHIA_PATH + MODEL_DIR
        // table_{corner}.txt
        // table_0.003.txt -> 0.00572394 4710 0.003
        this.PYTHIA_RUNNER = properties.getPythiaRunner();
        this.PYTHIA_RESULT_FORMAT = pythiaPath + modelDir + "/table_%s.txt";
    }

    @Override
    public String calculate(final PythiaRequest request) {
        String result = StringUtils.EMPTY;
        try {
            Process process = startProcess(request);
            log.info("PID: has started: " + process.pid());
            result = getProcessResultSync(process.getInputStream());
            log.info("Result: " + result);
            waitEndOfProcessSync(process);
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
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
            log.error("Calculated result is blank");
            return StringUtils.EMPTY;
        }
        if (!result.startsWith("Cross:")) {
            log.error("Calculated result has incorrect format");
            return StringUtils.EMPTY;
        }
        String[] split = result.split("Cross:");
        String value = split[1];
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.error("Calculated result has incorrect format", e);
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
            log.info("PID: has stopped: " + ph.pid());
        });
    }
}
