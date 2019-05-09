package ru.ilia.controller.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WsPointsRequest {

    @Valid
    @NotNull
    private String ksi;

    public String getKsi() {
        return ksi;
    }
}

