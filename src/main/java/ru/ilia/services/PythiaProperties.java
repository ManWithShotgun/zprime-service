package ru.ilia.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("pythia")
public class PythiaProperties {

    private String pythiaPath;
    private String modelDir;
    private String pythiaRunner;

    public String getPythiaPath() {
        return pythiaPath;
    }

    public void setPythiaPath(String pythiaPath) {
        this.pythiaPath = pythiaPath;
    }

    public String getModelDir() {
        return modelDir;
    }

    public void setModelDir(String modelDir) {
        this.modelDir = modelDir;
    }

    public String getPythiaRunner() {
        return pythiaRunner;
    }

    public void setPythiaRunner(String pythiaRunner) {
        this.pythiaRunner = pythiaRunner;
    }
}
