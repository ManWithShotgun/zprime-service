package ru.ilia.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ilia.exception.PythiaCalculationException;
import ru.ilia.services.PythiaProperties;
import ru.ilia.services.PythiaRequest;
import ru.ilia.services.PythiaResult;
import ru.ilia.services.PythiaService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * The class sync run sh as process with ksi and mass values.
 * Parse process output and returns calculated result.
 * <p>
 * read data from folder PYTHIA_PATH + MODEL_DIR
 * table_{corner}.txt
 * table_0.003.txt -> 0.00572394 4710 0.003
 * Example sh run: cd app/pythia8226_export/zprime/ && ./calc_zprime 0.003 4710
 */
@Slf4j
@Service
public class PythiaServiceImpl implements PythiaService {

    private final PythiaProperties properties;

    @Autowired
    public PythiaServiceImpl(PythiaProperties properties) {
        this.properties = properties;
    }

    @Override
    public String calculate(final PythiaRequest request) {
        try {
            Process process = startProcess(request);
            log.info("PID: was run: " + process.pid());
            String result = getProcessResultSync(process.getInputStream());
            log.info("Result: " + result);
            waitEndOfProcessSync(process);
            return result;
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PythiaCalculationException(e);
        }
    }

    private Process startProcess(PythiaRequest request) throws IOException {
        String[] commands = new String[]{properties.getPythiaRunner(), request.getKsi(), request.getMass()};
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
        return new PythiaResult(lastLine).getResult();
    }


    /**
     * The method is useless when sync reader was called before
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
