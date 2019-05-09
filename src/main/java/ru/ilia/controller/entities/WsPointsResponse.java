package ru.ilia.controller.entities;

import java.util.Map;

public class WsPointsResponse {

    private final String ksi;
    private final Map<String, String> result;

    public WsPointsResponse(String ksi, Map<String, String> result) {
        this.ksi = ksi;
        this.result = result;
    }

    public String getKsi() {
        return ksi;
    }

    public Map<String, String> getResult() {
        return result;
    }
}

