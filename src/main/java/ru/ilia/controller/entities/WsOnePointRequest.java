package ru.ilia.controller.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WsOnePointRequest {

    @Valid
    @NotNull
    private String ksi;
    @Valid
    @NotNull
    private String mass;
    @Valid
    @NotNull
    private String events;
    @Valid
    @NotNull
    private String cycles;

    public String getKsi() {
        return ksi;
    }

    public String getMass() {
        return mass;
    }

    public String getEvents() {
        return events;
    }

    public String getCycles() {
        return cycles;
    }
}
