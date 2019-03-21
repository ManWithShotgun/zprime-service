package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ZprimeService {

    private static final Logger logger = LoggerFactory.getLogger(ZprimeService.class);

    @Autowired
    public Environment env;

    public int abc = 133;

    @RequestMapping("/")
    public String home() {
        send();

        logger.warn("Test hot reload");
        logger.warn("das222");
        abc++;
        return "Hello Docker World" + abc;
    }

    public void send() {
        // "PYTHIA_PATH" from RunApplication config
        if (env.getProperty("PYTHIA_PATH") != null) {
            logger.warn("env: " + env.getProperty("PYTHIA_PATH"));
            String pythiaPath = env.getProperty("PYTHIA_PATH");
            String modelDir = env.getProperty("MODEL_DIR");
            // read data from folder PYTHIA_PATH + MODEL_DIR
            // table_{corner}.txt
            // table_0.003.txt -> 0.00572394 4710 0.003
            String pythiaRunnerSh = env.getProperty("PYTHIA_RUNNER");
            String zprimePath = pythiaPath + modelDir;
            execBat(pythiaRunnerSh);
//            Resource batFile = new ClassPathResource("/static/start.bat");
//            execBat(batFile.getURI().getPath());
        }
    }

    private void execBat(String pathSh) {
        try {
            // (cd app/pythia8226_export/zprime/ && ./calc_zprime 0.003 4710)
            String[] commands = new String[]{pathSh, "0.003", "4710"};
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
