package ru.ilia.services.impl;

import hello.ZprimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ilia.services.PythiaService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PythiaServiceImpl implements PythiaService {

    private static final Logger logger = LoggerFactory.getLogger(PythiaServiceImpl.class);

    private final String PYTHIA_RESULT_DIR;
    private final String PYTHIA_RUNNER;

    @Autowired
    public Environment env;


    // "PYTHIA_PATH" from RunApplication config
    public PythiaServiceImpl() {
        if (env.getProperty("PYTHIA_PATH") == null) {
            // if empty then exception
        }
        logger.warn("env: " + env.getProperty("PYTHIA_PATH"));
        String pythiaPath = env.getProperty("PYTHIA_PATH");
        String modelDir = env.getProperty("MODEL_DIR");
        // read data from folder PYTHIA_PATH + MODEL_DIR
        // table_{corner}.txt
        // table_0.003.txt -> 0.00572394 4710 0.003
        this.PYTHIA_RUNNER = env.getProperty("PYTHIA_RUNNER");
        this.PYTHIA_RESULT_DIR = pythiaPath + modelDir;
//            execBat(pythiaRunnerSh);
//            Resource batFile = new ClassPathResource("/static/start.bat");
//            execBat(batFile.getURI().getPath());
    }

    @Override
    public void calculate(double ksi, double mass) {
        try {
            // (cd app/pythia8226_export/zprime/ && ./calc_zprime 0.003 4710)
            String[] commands = new String[]{PYTHIA_RUNNER, "0.003", "4710"};
//            ProcessBuilder processBuilder = new ProcessBuilder("java", "-version");
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = processBuilder.start();
            CompletableFuture<Process> onProcessExit = process.onExit();
            process = onProcessExit.get(); // sync mode
            onProcessExit.thenAccept(ph -> {
                System.out.println("PID: has stopped: " + ph.pid());
            });
            System.out.println(process.pid());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
