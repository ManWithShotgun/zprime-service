package ru.ilia.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("pythia")
@Validated
public class PythiaProperties {

    @NotBlank
    private String pythiaPath;
    @NotBlank
    private String modelDir;
    @NotBlank
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
